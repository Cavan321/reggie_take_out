package com.example.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie.pojo.Employee;

/**
 * @author Cavan
 * @date 2023-03-30
 * @qq 2069543852
 */
public interface EmployeeService extends IService<Employee> {
    //员工信息分页展示，带条件
    Page<Employee> getEmployeeByOpr(Page<Employee> pageParam, String name);
}
