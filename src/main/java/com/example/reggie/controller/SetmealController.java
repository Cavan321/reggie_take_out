package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.dto.SetmealDto;
import com.example.reggie.pojo.Category;
import com.example.reggie.pojo.Setmeal;
import com.example.reggie.service.CategoryService;
import com.example.reggie.service.SetmealService;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.reggie.commen.R;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Cavan
 * @date 2023-04-17
 * @qq 2069543852
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    //请求网址: http://localhost:8080/setmeal/status/0?ids=1647958289454510081
    //请求方法: POST
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable("status") Integer status, @RequestParam List<Long> ids){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ids != null, Setmeal::getId, ids);
        List<Setmeal> list = setmealService.list(queryWrapper);
        for (Setmeal setmeal : list){
            if (setmeal != null){
                setmeal.setStatus(status);
                setmealService.updateById(setmeal);
            }
        }
        return R.success("售卖状态修改成功");
    }


    //请求网址: http://localhost:8080/setmeal?ids=1647902332846379009
    //请求方法: DELETE
    /**
     * @Description 删除套餐（包含批量删除）
     * @Param ids
     * @Retuen String
     * @Date 2023/4/17
     * @Author Cavan
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids：{}",ids);
        setmealService.removeWithDish(ids);
        return R.success("套餐删除成功");
    }



    //请求网址: http://localhost:8080/setmeal
    //请求方法: POST
    @ApiOperation("新增套餐")
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息：",setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    //请求网址: http://localhost:8080/setmeal/page?page=1&pageSize=10
    //请求方法: GET
    /*
     *@Description: 套餐分页查询
     *@Author: Cavan
     *@Date: 2023/4/17
     */
    @ApiOperation("套餐分页查询")
    @GetMapping("/page")
    public R<Page> getPage(Integer page, Integer pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name)
                .orderByDesc(Setmeal::getUpdateTime);
        Page<Setmeal> page1 = setmealService.page(setmealPage, queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(setmealPage, dtoPage, "records");
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> list = records.stream().map((i) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(i, setmealDto);
            Long categoryId = i.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }


}
