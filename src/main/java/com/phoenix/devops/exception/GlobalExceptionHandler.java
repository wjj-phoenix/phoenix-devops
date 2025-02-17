package com.phoenix.devops.exception;

import cn.hutool.core.collection.CollUtil;
import com.phoenix.devops.enums.RespEnum;
import com.phoenix.devops.lang.Result;
import com.phoenix.devops.utils.JsonUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Nullable> doException(Exception ex) {
        log.error("服务出现的异常: {}", ex.getMessage());
        ex.printStackTrace(System.err);
        return Result.failure(RespEnum.UNKNOWN);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Nullable> doException(HttpMessageNotReadableException ex) {
        log.error("服务读取消息出现异常: {}", ex.getMessage());
        return Result.failure("Http 消息不可读异常");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public Result<Nullable> doAuthenticationException(AuthenticationException ex) {
        log.error("认证失败异常:{}", ex.getMessage());
        return Result.failure(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalStateException.class)
    public Result<Nullable> doIllegalStateException(IllegalStateException ex) {
        log.error("运行服务出现的IllegalStateException异常:{}", ex.getMessage());
        return Result.failure(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Nullable> doIllegalArgumentException(IllegalArgumentException ex) {
        log.error("运行服务出现的IllegalArgumentException异常:{}", ex.getMessage());
        return Result.failure(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result<Nullable> doMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, Object> map = new HashMap<>();
        if (CollUtil.isNotEmpty(bindingResult.getFieldErrors())) {
            bindingResult.getFieldErrors().forEach(fieldError -> {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            });
        } else {
            bindingResult.getAllErrors().forEach(n -> {
                map.put(n.getObjectName(), n.getDefaultMessage());
            });
        }
        return Result.failure(JsonUtil.toJSONString(map));
    }

    /**
     * BindException异常处理
     * <p>BindException: 作用于@Validated @Valid 注解，仅对于表单提交有效，对于以json格式提交将会失效</p>
     *
     * @param e BindException异常信息
     * @return 响应数据
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Result<?> bindExceptionHandler(BindException e) {
        String msg = e.getBindingResult().getFieldErrors()
                .stream()
                .map(n -> String.format("%s: %s", n.getField(), n.getDefaultMessage()))
                .reduce((x, y) -> String.format("%s; %s", x, y))
                .orElse("参数输入有误");

        log.error("BindException异常，参数校验异常：{}", msg);
        return Result.failure(msg);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({ConstraintViolationException.class})
    public Result<Nullable> doConstraintViolationException(ConstraintViolationException ex) {
        return Result.failure(ex.getMessage());
    }
}
