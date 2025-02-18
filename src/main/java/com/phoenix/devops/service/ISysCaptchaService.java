package com.phoenix.devops.service;

import com.phoenix.devops.lang.Result;
import com.phoenix.devops.model.vo.CaptchaVO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

/**
 * @author wjj-phoenix
 * @since 2025-02-18
 */
public interface ISysCaptchaService {
    CaptchaVO get();

    CaptchaVO check(@NotNull CaptchaVO var1);

    Result<Nullable> verification(CaptchaVO captchaVO);
}
