package com.phoenix.devops.lang;

import lombok.Data;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 * 字段错误信息
 */
@Data
public class FieldError {
    /**
     * 字段名称
     */
    private final String fieldName;

    /**
     * 错误信息
     */
    private final String errorMessage;

    public FieldError(String fieldName, String errorMessage) {
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }

    public static FieldError of(String fieldName, String errorMessage) {
        return new FieldError(fieldName, errorMessage);
    }
}
