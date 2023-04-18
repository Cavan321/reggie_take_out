package com.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.mapper.EmployeeMapper;
import com.example.reggie.pojo.Employee;
import com.example.reggie.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author Cavan
 * @date 2023-03-30
 * @qq 2069543852
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {


    //员工信息分页展示，带条件
    @Override
    public Page<Employee> getEmployeeByOpr(Page<Employee> pageParam, String name) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //如果name不为空，则进行模糊查询
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //通过更新时间进行排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        Page<Employee> page = baseMapper.selectPage(pageParam, queryWrapper);

        return page;
    }
}
