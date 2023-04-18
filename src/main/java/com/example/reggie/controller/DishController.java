package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.commen.R;
import com.example.reggie.dto.DishDto;
import com.example.reggie.pojo.Category;
import com.example.reggie.pojo.Dish;
import com.example.reggie.service.CategoryService;
import com.example.reggie.service.DishFlavorService;
import com.example.reggie.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Cavan
 * @date 2023-04-01
 * @qq 2069543852
 */
@Api(tags = "菜品管理器")
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    //请求网址: http://localhost:8080/dish/list?categoryId=1641684064901087234
    //请求方法: GET
    //根据条件查询对应的菜品数据

    @ApiOperation("根据条件查询对应的菜品数据")
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId())
                .eq(Dish::getStatus, 1)
                .orderByAsc(Dish::getSort)
                .orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }


    /*请求 URL: http://localhost:8080/dish
      请求方法: POST
      */
    @ApiOperation("新增菜品")
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("disdto:{}", dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /*请求 URL: http://localhost:8080/dish/page?page=1&pageSize=10
        请求方法: GET*/
    @ApiOperation("菜品分页查询，带条件")
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String name) {

        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage, queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");

        List<Dish> records = dishPage.getRecords();
        List<DishDto> list = records.stream().map((i) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(i, dishDto);
            Long categoryId = i.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    @ApiOperation("根据id来查询菜品信息，和对应的口味信息")
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /*请求 URL: http://localhost:8080/dish
      请求方法: PUT*/
    @ApiOperation("更新菜品信息和口味信息")
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功");
    }

    /*请求 URL: http://localhost:8080/dish?ids=1397860242057375745
        请求方法: DELETE*/
    @DeleteMapping
    @ApiOperation("根据id删除信息")
    public R<String> deleteDish(String ids) {
        dishService.deleteDish(ids);
        return R.success("删除成功");
    }


    //菜品的停售、起售（包括批量停售和起售）
    @PostMapping("/status/{status}")
    //这个参数这里一定记得加注解才能获取到参数，否则这里非常容易出问题
    public R<String> status(@PathVariable("status") Integer status,@RequestParam List<Long> ids){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(ids !=null,Dish::getId,ids);
        //根据传入的id集合进行批量查询
        List<Dish> list = dishService.list(queryWrapper);
        for (Dish dish : list) {
            if (dish != null){
                dish.setStatus(status);
                dishService.updateById(dish);
            }
        }
        return R.success("售卖状态修改成功");
    }


}
