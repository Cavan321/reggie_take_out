package com.example.reggie.commen;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Cavan
 * @date 2023-03-30
 * @qq 2069543852
 */

/*
 *@Description: 自定义元数据处理器
 *@Author: Cavan
 *@Date: 2023/3/30
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {


    /*
     *@Description: 插入操作自动填充
     *@Author: Cavan
     *@Date: 2023/3/30
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充【insert】...");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }

    /*
     *@Description: 更新操作，自动填充
     *@Author: Cavan
     *@Date: 2023/3/30
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充【update】...");
        log.info(metaObject.toString());

        long id = Thread.currentThread().getId();
        log.info("线程id为：{}", id);

        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
