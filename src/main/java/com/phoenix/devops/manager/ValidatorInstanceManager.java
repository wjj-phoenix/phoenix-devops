package com.phoenix.devops.manager;

import com.phoenix.devops.annotation.SpELConstraint;
import com.phoenix.devops.exception.SpELValidatorException;
import com.phoenix.devops.validator.SpELConstraintValidator;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 */
public class ValidatorInstanceManager {
    private ValidatorInstanceManager() {
    }

    /**
     * 校验器实例管理器，避免重复创建校验器实例。
     */
    private static final ConcurrentHashMap<Annotation, SpELConstraintValidator<?>> VALIDATOR_INSTANCE_CACHE = new ConcurrentHashMap<>();

    /**
     * 获取校验器实例，当实例不存在时会创建一个新的实例。
     */
    @NotNull
    public static SpELConstraintValidator<? extends Annotation> getInstance(@NotNull Annotation annotation) {
        return VALIDATOR_INSTANCE_CACHE.computeIfAbsent(annotation, key -> {
            try {
                Class<? extends Annotation> annoClazz = annotation.annotationType();
                SpELConstraint constraint = annoClazz.getAnnotation(SpELConstraint.class);
                if (constraint == null) {
                    throw new SpELValidatorException("Annotation [" + annoClazz.getName() + "] is not a Spel Constraint annotation");
                }
                Class<? extends SpELConstraintValidator<?>> validatorClass = constraint.validatedBy();
                return validatorClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new SpELValidatorException("Failed to create validator instance, annotation [" + annotation.annotationType().getName() + "]", e);
            }
        });
    }
}
