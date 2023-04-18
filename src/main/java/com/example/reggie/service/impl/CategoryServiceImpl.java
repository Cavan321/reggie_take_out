package com.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.commen.CustomException;
import com.example.reggie.mapper.CategoryMapper;
import com.example.reggie.pojo.Category;
import com.example.reggie.pojo.Dish;
import com.example.reggie.pojo.Setmeal;
import com.example.reggie.service.CategoryService;
import com.example.reggie.service.DishService;
import com.example.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Cavan
 * @date 2023-03-31
 * @qq 2069543852
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {


    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public Page<Category> getCategoryByOpr(Page<Category> pageParam) {

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        Page<Category> page = baseMapper.selectPage(pageParam, queryWrapper);
        return page;
    }

    //根据id来删除分类，删除之前判断是否关联的菜品
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, id);
        int count = dishService.count(queryWrapper);
        //查询当前菜品分类是否关联的菜品，如果已经关联，抛出一个业务异常
        if (count > 0) {
            //已经关联菜品，抛出一个业务异常
            throw new CustomException("当前分类项关联了菜品，不能删除");
        }
        //查询当前菜品分类是否关联的套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Setmeal::getCategoryId, id);
        int count1 = setmealService.count(queryWrapper1);
        if (count1 > 0) {
            //已经关联套餐，抛出一个业务异常
            throw new CustomException("当前分类项关联了套餐，不能删除");
        }
        //正常删除
        super.removeById(id);
    }


}
