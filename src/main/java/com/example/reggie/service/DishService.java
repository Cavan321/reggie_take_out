package com.example.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie.dto.DishDto;
import com.example.reggie.pojo.Dish;

/**
 * @author Cavan
 * @date 2023-03-31
 * @qq 2069543852
 */
public interface DishService extends IService<Dish> {

    //新增菜品，同时插入菜品对应的口味数据，需要操作两张表dish  dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //根据id来查询菜品信息，和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);


    //更新菜品信息和口味信息
    public void updateWithFlavor(DishDto dishDto);


    void deleteDish(String ids);
}
