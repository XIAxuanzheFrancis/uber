package com.xuanzhe.filter;

import com.alibaba.fastjson.JSON;
import com.xuanzhe.common.R;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
  public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest)servletRequest;
    HttpServletResponse resp = (HttpServletResponse) servletResponse;

    String requestURI = req.getRequestURI();
    String[] urls = new String[]{
        "/employee/login",
        "/employee/logout",
        "/backend/**",
        "/front/**"
    };
    boolean check = check(urls,requestURI);

    if(check){
      filterChain.doFilter(req,resp);
      return;
    }

    if(req.getSession().getAttribute("employee")!=null){
      filterChain.doFilter(req,resp);
      return;
    }
    //log.info("Intercepting a request: {}",req.getRequestURI());
    resp.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    return;
  }

  public boolean check(String[] urls, String requetURI){
    for (String url : urls) {
      boolean match = PATH_MATCHER.match(url,requetURI);
      if(match){
        return true;
      }
    }
    return false;
  }
}
