package com.phoenix.devops.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.*;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 * 字段不可以被修改
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Immutable.ImmutableFieldValidator.class)
public @interface Immutable {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Slf4j
    class ImmutableFieldValidator implements ConstraintValidator<Immutable, Object> {
        @Override
        public boolean isValid(Object obj, ConstraintValidatorContext context) {
            return obj == null;
        }
    }
}
