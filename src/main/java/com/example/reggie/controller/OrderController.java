package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.commen.R;
import com.example.reggie.pojo.Orders;
import com.example.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Cavan
 * @date 2023-04-17
 * @qq 2069543852
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrdersService ordersService;


    /**
     * 修改订单状态
     */
    @PutMapping
    public R<String> orderStatusChange(@RequestBody Map<String,String> map){

        String id = map.get("id");
        Long orderId = Long.parseLong(id);//将接收到的id转为Long型
        Integer status = Integer.parseInt(map.get("status"));//转为Integer型

        if(orderId == null || status==null){
            return R.error("传入信息非法");
        }
        Orders orders = ordersService.getById(orderId);//根据订单id查询订单数据
        orders.setStatus(status);//修改订单对象里的数据
        ordersService.updateById(orders);

        return R.success("订单状态修改成功");

    }


    //请求网址: http://localhost:8080/order/page?page=1&pageSize=10
    //请求方法: GET
    /**
     * 后台显示订单信息
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, Long number, String beginTime, String endTime) {
        log.info("page={},pageSize={},number={},beginTime={},endTime={}",page,pageSize,number,beginTime,endTime);
        //分页构造器对象
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        //构造条件查询对象
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //链式编程写查询条件
        queryWrapper.like(number!=null,Orders::getNumber,number)
                //前面加上判定条件是十分必要的，用户没有填写该数据，查询条件上就不添加它
                .gt(StringUtils.isNotBlank(beginTime),Orders::getOrderTime,beginTime)//大于起始时间
                .lt(StringUtils.isNotBlank(endTime),Orders::getOrderTime,endTime);//小于结束时间
        ordersService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }


}
