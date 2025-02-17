package com.phoenix.devops.annotation;

import java.lang.annotation.*;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoRespAdvice {
}
