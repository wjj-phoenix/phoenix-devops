package com.phoenix.devops.model.code;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 * 扩展了{@link ValueEnum}接口. 带有枚举值以及枚举名称的枚举接口(可使用{@link com.phoenix.devops.utils.EnumUtil}中的方法。
 */
public interface NameValueEnum<T> extends ValueEnum<T> {
    /**
     * 获取枚举名称
     *
     * @return 枚举名
     */
    String getName();
}
