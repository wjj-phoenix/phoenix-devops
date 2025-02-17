package com.phoenix.devops.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * @author wjj-phoenix
 * @since 2025-02-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo {
    @NotBlank(message = "登录账号不能为空")
    @Length(min = 4, max = 16, message = "账号长度为 4-16 位")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "账号格式为数字以及字母")
    @Schema(description = "账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "root")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(min = 4, max = 16, message = "密码长度为 4-16 位")
    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "wjj123456")
    private String password;

    // ========== 图片验证码相关 ==========
    // @NotBlank(message = "验证码不能为空")
    @Schema(description = "验证码，验证码开启时，需要传递", requiredMode = Schema.RequiredMode.REQUIRED, example = "PfcH6mgr8tpXuMWFjvW6YVaqrswIuwmWI5dsVZSg7sGpWtDCUbHuDEXl3cFB1+VvCC/rAkSwK8Fad52FSuncVg==")
    private String captchaVerification;

    @Schema(description = "授权码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String socialCode;

    @Schema(description = "state", requiredMode = Schema.RequiredMode.REQUIRED, example = "9b2ffbc1-7425-4155-9894-9d5c08541d62")
    private String socialState;

    /**
     * 开启验证码的 Group
     */
    public interface CodeEnableGroup {}
}
