package com.phoenix.devops.exception;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 * SpEL 验证器异常
 */
public class SpELValidatorException extends RuntimeException {

    public SpELValidatorException(String message) {
        super(message);
    }

    public SpELValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpELValidatorException(Throwable cause) {
        super(cause);
    }

}
