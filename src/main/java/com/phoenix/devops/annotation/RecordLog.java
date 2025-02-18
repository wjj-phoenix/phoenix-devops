package com.phoenix.devops.annotation;

import com.mybatisflex.core.service.IService;
import com.phoenix.devops.enums.OperationType;

import java.lang.annotation.*;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RecordLog {
    // 查询数据库所调用的class文件 selectById方法所在的Service类
    Class<? extends IService> service() default IService.class;

    // 是否需要比较新旧数据
    boolean isCompare() default true;

    // id的类型
    Class<Long> idType() default Long.class;

    // 操作对象的id字段名称
    String primaryKey() default "id";

    String title() default "";

    // 操作类型 add update delete
    OperationType type() default OperationType.UPDATE;
}