package com.phoenix.devops.annotation;

import cn.hutool.core.collection.CollUtil;
import com.phoenix.devops.model.code.IntArrayValuable;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 */
@Documented
@Target({
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {InEnum.InEnumValidator.class, InEnum.InEnumCollectionValidator.class})
public @interface InEnum {
    /**
     * @return 实现 EnumValuable 接口的
     */
    Class<? extends IntArrayValuable> value();

    String message() default "必须在指定范围 {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class InEnumValidator implements ConstraintValidator<InEnum, Integer> {
        private List<Integer> values;

        @Override
        public void initialize(InEnum annotation) {
            IntArrayValuable[] values = annotation.value().getEnumConstants();
            if (values.length == 0) {
                this.values = Collections.emptyList();
            } else {
                this.values = Arrays.stream(values[0].array()).boxed().collect(Collectors.toList());
            }
        }

        @Override
        public boolean isValid(Integer value, ConstraintValidatorContext context) {
            // 为空时，默认不校验，即认为通过
            if (value == null) {
                return true;
            }
            // 校验通过
            if (values.contains(value)) {
                return true;
            }
            // 校验不通过，自定义提示语句（因为，注解上的 value 是枚举类，无法获得枚举类的实际值）
            // 禁用默认的 message 的值
            context.disableDefaultConstraintViolation();
            // 重新添加错误提示语句
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate().replaceAll("\\{value}", values.toString())).addConstraintViolation();
            return false;
        }
    }

    class InEnumCollectionValidator implements ConstraintValidator<InEnum, Collection<Integer>> {
        private List<Integer> values;

        @Override
        public void initialize(InEnum annotation) {
            IntArrayValuable[] values = annotation.value().getEnumConstants();
            if (values.length == 0) {
                this.values = Collections.emptyList();
            } else {
                this.values = Arrays.stream(values[0].array()).boxed().collect(Collectors.toList());
            }
        }

        @Override
        public boolean isValid(Collection<Integer> list, ConstraintValidatorContext context) {
            // 校验通过
            if (CollUtil.containsAll(values, list)) {
                return true;
            }
            // 校验不通过，自定义提示语句（因为，注解上的 value 是枚举类，无法获得枚举类的实际值）
            // 禁用默认的 message 的值
            context.disableDefaultConstraintViolation();
            // 重新添加错误提示语句
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()
                    .replaceAll("\\{value}", CollUtil.join(list, ","))).addConstraintViolation();
            return false;
        }
    }
}