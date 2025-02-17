package com.phoenix.devops.annotation;

import com.phoenix.devops.lang.FieldValidResult;
import com.phoenix.devops.validator.SpELConstraintValidator;

import java.lang.annotation.*;
import java.lang.reflect.Field;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 * 字段值存在性校验
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@SpELConstraint(validatedBy = Existence.ExistenceFieldValidator.class)
public @interface Existence {

    class ExistenceFieldValidator implements SpELConstraintValidator<Existence> {
        @Override
        public FieldValidResult isValid(Existence annotation, Object obj, Field field) throws IllegalAccessException {
            return FieldValidResult.success();
        }
    }
}
