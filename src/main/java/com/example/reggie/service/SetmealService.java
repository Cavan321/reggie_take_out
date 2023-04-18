package com.example.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie.dto.SetmealDto;
import com.example.reggie.pojo.Setmeal;

import java.util.List;

/**
 * @author Cavan
 * @date 2023-03-31
 * @qq 2069543852
 */
public interface SetmealService extends IService<Setmeal> {

    //新增套餐，同时需要保存套餐和菜品的关联关系
    public void saveWithDish(SetmealDto setmealDto);
    
    /**
     * @Description 删除套餐，同时需要删除套餐和菜品的关联数据
     * @Param ids
     * @Date 2023/4/17
     * @Author Cavan
     */
    public void removeWithDish(List<Long> ids);

}
