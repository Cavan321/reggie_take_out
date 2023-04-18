package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.commen.R;
import com.example.reggie.pojo.Employee;
import com.example.reggie.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author Cavan
 * @date 2023-03-30
 * @qq 2069543852
 */
@Api(tags = "员工管理器")
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @ApiOperation("根据id查询员工信息")
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if (employee == null){
            return R.error("没有查询到对应员工信息");
        }
        return R.success(employee);
    }


    //根据id修改员工信息
    @ApiOperation("根据id修改员工信息")
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());

        long id = Thread.currentThread().getId();
        log.info("线程id为：{}",id);

//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }


    //GET   employee/page?page=1&pageSize=10
    @ApiOperation("员工信息分页展示，带条件")
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String name) {
        log.info("page = {},pageSize = {},name = {}", page, pageSize, name);
        //构造分页构造器
        Page<Employee> pageParam = new Page<>(page, pageSize);
        //构造条件构造器
        Page<Employee> page1 = employeeService.getEmployeeByOpr(pageParam, name);
        return R.success(page1);
    }


    //http://localhost:8080/employee
    @ApiOperation("新增员工")
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工，员工信息：{}", employee.toString());
        //设置初始密码为123456，并进行MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }


    //POST  employee/logout
    @ApiOperation("员工退出")
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清楚Session中保存到的当前员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    //POST  employee/login
    @ApiOperation("员工登录")
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //1.将页面提交的密码进行MD5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //3.如果没有查询到，则返回登录失败的登录结果
        if (emp == null) {
            return R.error("用户名或密码错误");
        }
        //4.比对密码，若不一致，返回失败的登录结果
        if (!emp.getPassword().equals(password)) {
            return R.error("用户名或密码错误");
        }
        //5.查看员工在状态，判断员工是否被禁用
        if (emp.getStatus() != 1) {
            return R.error("账号已被锁定");
        }
        //6.登录成功，将员工id存入session域并返回登录成功的结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }


}
