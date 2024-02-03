package com.xuanzhe.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.xuanzhe.common.R;
import com.xuanzhe.pojo.Employee;
import com.xuanzhe.service.EmployeeService;
import com.xuanzhe.service.impl.EmployeeServiceImpl;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
  @Autowired
  private EmployeeService employeeService;

  @PostMapping("/login")
  public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
    String password = employee.getPassword();
    password = DigestUtils.md5DigestAsHex(password.getBytes());

    LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Employee::getUsername, employee.getUsername());
    Employee emp = employeeService.getOne(queryWrapper);


    if(emp == null)
      return R.error("Login Failure");
    else if(!emp.getPassword().equals(password))
      return R.error("Login Failure");
    else if(emp.getStatus()==0){
      return R.error("Account disabled");
    }
    request.getSession().setAttribute("employee",emp.getId());
    return R.success(emp);
  }

  @PostMapping("/logout")
  public R<String> logout(HttpServletRequest request){
    request.getSession().removeAttribute("employee");
    return R.success("Exit successful");
  }

}
