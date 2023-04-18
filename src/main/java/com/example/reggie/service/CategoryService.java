package com.example.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie.pojo.Category;

/**
 * @author Cavan
 * @date 2023-03-31
 * @qq 2069543852
 */
public interface CategoryService extends IService<Category> {
    Page<Category> getCategoryByOpr(Page<Category> pageParam);

    public void remove(Long id);

}
