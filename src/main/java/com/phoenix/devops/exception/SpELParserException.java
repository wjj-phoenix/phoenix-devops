package com.phoenix.devops.exception;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 * SpEl 表达式解析异常
 */
public class SpELParserException extends SpELValidatorException {

    public SpELParserException(String message) {
        super(message);
    }

    public SpELParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpELParserException(Throwable cause) {
        super(cause);
    }

}
