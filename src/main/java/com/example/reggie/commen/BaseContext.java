package com.example.reggie.commen;

/**
 * @author Cavan
 * @date 2023-03-31
 * @qq 2069543852
 */
/*
 *@Description: 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 *@Author: Cavan
 *@Date: 2023/3/31
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

    /*
    *@Description: 设置值值
    *@Param: id
    *@Author: Cavan
    *@Date: 2023/3/31
    */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /*
    *@Description: 获取值id
    *@Param: id
    *@Author: Cavan
    *@Date: 2023/3/31
    */
    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
