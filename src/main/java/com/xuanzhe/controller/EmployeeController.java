package com.xuanzhe.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuanzhe.common.R;
import com.xuanzhe.pojo.Employee;
import com.xuanzhe.service.EmployeeService;
import com.xuanzhe.service.impl.EmployeeServiceImpl;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

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

  @PostMapping
  public R<String> save(HttpServletRequest req, @RequestBody Employee employee){
    log.info("add employee's informations: {}", employee.toString());
    employee.setPassword(DigestUtils.md5DigestAsHex(("123456".getBytes())));
    employee.setCreateTime(LocalDateTime.now());
    employee.setUpdateTime(LocalDateTime.now());
    Long empId = (Long)req.getSession().getAttribute("employee");
    employee.setCreateUser(empId);
    employee.setUpdateUser(empId);
    employeeService.save(employee);
    return R.success("Add Employee Success");
  }

  @GetMapping("/page")
  public R<Page> page(int page, int pageSize, String name){
    log.info("page={}, pageSize={}, name={}",page,pageSize,name);
    Page pageInfo = new Page(page, pageSize);

    LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();
    lambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);

    lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);

    employeeService.page(pageInfo, lambdaQueryWrapper);
    return R.success(pageInfo);
  }

}
