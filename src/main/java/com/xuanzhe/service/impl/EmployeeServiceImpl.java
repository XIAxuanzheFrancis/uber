package com.xuanzhe.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuanzhe.mapper.EmployeeMapper;
import com.xuanzhe.pojo.Employee;
import com.xuanzhe.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements
    EmployeeService {

}
