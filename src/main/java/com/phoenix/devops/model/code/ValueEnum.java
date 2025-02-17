package com.phoenix.devops.model.code;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 * 最简单的枚举类,即只含value的枚举,实现此接口的枚举类可在如下场景中应用:
 * <p>1. 使用{@link com.phoenix.devops.utils.EnumUtil}中的方法来消除枚举枚举类中重复冗余的代码
 * <p>2. {@link @EnumValue }注解,用来校验入参枚举值的正确性.
 */
public interface ValueEnum<T> {

    /**
     * 获取枚举值
     *
     * @return 枚举值
     */
    T getValue();
}
