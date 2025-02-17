package com.phoenix.devops.exception;

import lombok.Getter;

import java.util.Set;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 * 不支持的类型异常
 */
@Getter
public class SpELNotSupportedTypeException extends SpELValidatorException {
    private final Class<?> clazz;

    private final Set<Class<?>> supperType;

    public SpELNotSupportedTypeException(Class<?> clazz, Set<Class<?>> supperType) {
        super("Class type not supported, current type: " + clazz.getName() + ", supper type: " + supperType.toString());
        this.clazz = clazz;
        this.supperType = supperType;
    }
}
