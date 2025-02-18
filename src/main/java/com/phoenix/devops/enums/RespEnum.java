package com.phoenix.devops.enums;

import com.phoenix.devops.lang.Result;
import jakarta.annotation.Nullable;
import lombok.Getter;

import java.text.MessageFormat;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 * 响应枚举
 */
@Getter
public enum RespEnum {
    SUCCESS(0, "请求成功"),
    /**
     * 请求失败
     */
    BAD_REQUEST(400, "请求失败"),
    INVALID_PARAMETER(400, "参数错误，请检查相关参数"),
    /**
     * 认证失败/登录失效
     */
    UNAUTHORIZED(401, "认证失败/登录失效"),
    USERNAME_OR_PASSWORD_ERROR(401, "用户名或密码错误"),
    PASSWORD_ERROR(401, "用户名或密码错误"),
    VERIFY_CODE_TIMEOUT(401, "验证码已过期"),
    VERIFY_CODE_ERROR(401, "验证码错误"),
    VERIFY_CODE_NOT_NULL(401, "验证码不能为空"),
    REQUEST_HEADER_NULL(401, "授权请求头为空"),
    REQUEST_HEADER_ERROR(401, "错误的授权请求头"),
    ACCOUNT_STATUS_INVALID(401, "用户不可用或被禁用"),

    /**
     * 没有权限
     */
    ACCESS_UNAUTHORIZED(403, "访问未授权"),
    TOKEN_INVALID(403, "token无效或已过期"),
    TOKEN_ACCESS_FORBIDDEN(403, "token已被禁止访问"),
    /**
     * 请求资源不存在
     */
    NOT_FOUND(404, "请求资源不存在"),
    /**
     * 请求方式不允许
     */
    METHOD_NOT_ALLOWED(405, "请求方式不允许"),
    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(408, "请求超时"),
    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    /**
     * 远程服务器接收到了一个无效的响应
     */
    BAD_GATEWAY(502, "远程服务器接收到了一个无效的响应"),
    /**
     * 网关响应超时
     */
    GATEWAY_TIMEOUT(504, "网关响应超时"),
    /**
     * 自定义异常信息
     */
    CUSTOM_EXCEPTION(-1, ""),

    INVALID_ID(1000, "无效ID"),
    EXISTS(1001, "已存在"),
    NOT_EXISTS(1002, "不存在"),
    FILE_NOT_EXISTS(1004, "文件不存在"),
    FILE_UPLOAD_EXT_ERROR(1004, "上传文件类型错误"),
    FILE_UPLOAD_SIZE_ERROR(1005, "上传文件大小不能超过10MB"),
    FILE_UPLOAD_ERROR(1006, "上传文件失败"),
    USERNAME_EXISTS(1007, "用户名已存在"),
    INVALID(1008, "无效的数据"),

    VALID_ERROR(1100, "参数校验异常"),
    BAD_USERNAME_OR_PASSWORD(1101, "账户不存在或密码错误"),
    CNT_PASSWORD_ERR(1102, "密码错误次数过多，账户锁定！"),
    CLIENT_INVALID(1103, "无效的ClientId"),
    CLIENT_BLOCKED(1104, "Client认证已禁用"),
    INVALID_TOKEN(1105, "无效Token"),
    INVALID_USER(1106, "无效用户"),
    BAD_USERNAME_STATUS_INVALID(1107, "用户被禁用"),
    INVALID_PERMISSION(1108, "抱歉，您目前无权执行此操作，请联系管理员获取相应权限。"),
    WEBSOCKET_SEND_FAIL(1109, "WebSocket消息发送异常"),
    DEBOUNCE(1110, "您的请求过于频繁，请稍后再试！"),

    UNKNOWN(9999, "未知异常"),

    REPEAT_SUBMIT_ERROR(8888, "您的请求已提交，请不要重复提交或等待片刻再尝试。"),

    BLANK_ERROR(1101, "{0}不能为空"),
    NULL_ERROR(1102, "{0}不能为空"),
    NOT_NULL_ERROR(1102, "{0}必须为空"),
    NOT_EXIST_ERROR(1102, "{0}数据库中不存在"),
    EXIST_ERROR(1102, "{0}数据库中已存在"),
    PARAM_TYPE_ERROR(1102, "{0}类型错误"),
    PARAM_FORMAT_ERROR(1102, "{0}格式错误"),

    API_CAPTCHA_INVALID(1102, "验证码已失效，请重新获取"),
    API_CAPTCHA_COORDINATE_ERROR(1102, "验证失败"),
    API_CAPTCHA_ERROR(1102, "获取验证码失败,请联系管理员"),
    API_CAPTCHA_BASEMAP_NULL(1102, "底图未初始化成功，请检查路径"),

    API_REQ_LIMIT_GET_ERROR(1102, "get接口请求次数超限，请稍后再试!"),
    API_REQ_INVALID(1102, "无效请求，请重新获取验证码"),
    API_REQ_LOCK_GET_ERROR(1102, "接口验证失败数过多，请稍后再试"),
    API_REQ_LIMIT_CHECK_ERROR(1102, "check接口请求次数超限，请稍后再试!"),
    API_REQ_LIMIT_VERIFY_ERROR(1102, "verify请求次数超限!");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 响应信息
     */
    private final String message;

    RespEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 将入参fieldNames与this.desc组合成错误信息
     * {fieldName}不能为空
     *
     * @param fieldNames 入参
     * @return Result
     */
    public Result<Nullable> parseError(Object... fieldNames) {
        return Result.failure(MessageFormat.format(this.message, fieldNames));
    }
}