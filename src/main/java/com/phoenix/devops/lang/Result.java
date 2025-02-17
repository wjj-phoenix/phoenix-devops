package com.phoenix.devops.lang;

import com.phoenix.devops.enums.RespEnum;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 * 统一结构响应实体类
 */
@Getter
public final class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Parameter(description = "状态码")
    private final Integer code;
    @Parameter(description = "响应消息")
    private final String message;
    @Parameter(description = "响应体")
    private final T data;

    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private Result(RespEnum resp, T data) {
        this.code = resp.getCode();
        this.message = resp.getMessage();
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(RespEnum.SUCCESS, data);
    }

    public static <T> Result<T> failure(RespEnum resp) {
        return new Result<T>(resp, null);
    }

    public static <T> Result<T> failure(String message) {
        return new Result<>(10000, message, null);
    }

    public static <T> Result<T> failure(int status, String message) {
        return new Result<>(status, message, null);
    }
}
