package com.phoenix.devops.annotation;

import com.phoenix.devops.validator.SpELConstraintValidator;

import java.lang.annotation.*;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 */
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SpELConstraint {
    /**
     * 校验器的实现类，用于校验被标记的注解。
     */
    Class<? extends SpELConstraintValidator<?>> validatedBy();
}
