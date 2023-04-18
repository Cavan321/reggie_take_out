package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.commen.R;
import com.example.reggie.pojo.Category;
import com.example.reggie.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Cavan
 * @date 2023-03-31
 * @qq 2069543852
 */
@Api(tags = "分类管理器")
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @ApiOperation("新增菜品或套餐分类")
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("category：{}", category);
        categoryService.save(category);
        return R.success("新增成功");
    }

    //GET   category/page?page=1&pageSize=10
    @ApiOperation("分类管理")
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize) {
        log.info("page = {},pageSize = {}", page, pageSize);
        Page<Category> pageParam = new Page<>(page, pageSize);
//        方法一
//        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.orderByAsc(Category::getSort);
//        Page<Category> page2 = categoryService.page(pageParam, queryWrapper);
        //方法二
        Page<Category> page1 = categoryService.getCategoryByOpr(pageParam);
        return R.success(page1);
    }

    //DELETE   category?ids=1641690163880505346
    @ApiOperation("根据id删除分类")
    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("删除分类，id为：{}", ids);
//        categoryService.removeById(ids);
        categoryService.remove(ids);
        return R.success("分类信息删除成功");
    }

    //根据id修改分类信息
    @ApiOperation("根据id修改分类信息")
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("修改分类信息：{}", category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    /*请求 URL: http://localhost:8080/category/list?type=1
      请求方法: GET
     */
    @ApiOperation("根据条件查询分类数据")
    @GetMapping("/list")
    public R<List> list(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //如果type不为空，则添加查询条件
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }


}
