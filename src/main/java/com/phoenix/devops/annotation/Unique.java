package com.phoenix.devops.annotation;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import jakarta.annotation.Resource;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.*;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 * 字段在数据表中唯一
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {Unique.UniqueValidator.class})
public @interface Unique {
    // 查询数据库所调用的class文件
    Class<? extends IService<?>> service();

    /**
     * 字段名称
     *
     * @return 字段名
     */
    String field();

    /**
     * 校验失败时的错误信息
     *
     * @return str
     */
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class UniqueValidator implements ConstraintValidator<Unique, String> {
        private Unique unique;
        @Resource
        private ApplicationContext applicationContext;

        /**
         * 主要做一些初始化操作，它的参数是使用到的注解，可以获取到运行时的注解信息
         *
         * @param unique 自定义注解
         */
        @Override
        public void initialize(Unique unique) {
            this.unique = unique;
        }

        /**
         * 要实现的校验逻辑，被注解的对象会传入此方法中
         *
         * @param str                        需要检验的值
         * @param constraintValidatorContext 用于在执行自定义校验逻辑时向用户提供反馈
         * @return true|false
         */
        @Override
        public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
            String wrapperSql = String.format("%s=?", unique.field());
            IService<?> service = applicationContext.getBean(unique.service());
            return !service.exists(QueryWrapper.create().where(wrapperSql, str));
        }
    }
}
