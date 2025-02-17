package com.phoenix.devops.annotation;

import com.phoenix.devops.executor.SpELValidExecutor;
import com.phoenix.devops.lang.FieldError;
import com.phoenix.devops.lang.ObjectValidResult;
import com.phoenix.devops.parser.SpELParser;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.*;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {SpELValid.SpELValidator.class})
public @interface SpELValid {
    /**
     * 开启校验的前置条件，值必须为合法的 SpEL 表达式
     * <p>
     * 当 表达式为空 或 计算结果为true 时，表示开启校验
     */
    String condition() default "";

    /**
     * 分组功能，值必须为合法的 SpEL 表达式
     * <p>
     * 当分组信息为空时，表示不开启分组校验
     */
    String[] spELGroups() default {};

    String message() default "";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

    @Slf4j
    class SpELValidator implements ConstraintValidator<SpELValid, Object> {

        private SpELValid spelValid;

        @Override
        public void initialize(SpELValid constraintAnnotation) {
            this.spelValid = constraintAnnotation;
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }

            // 表达式不为空且计算结果为 false，跳过校验
            if (!spelValid.condition().isEmpty() && !SpELParser.parse(spelValid.condition(), value, Boolean.class)) {
                log.debug("SpelValid condition is not satisfied, skip validation, condition: {}", spelValid.condition());
                return true;
            }

            // 校验对象
            ObjectValidResult validateObjectResult = SpELValidExecutor.validateObject(value, spelValid.spELGroups());

            // 构建错误信息
            buildConstraintViolation(validateObjectResult, context);
            return validateObjectResult.noneError();
        }

        /**
         * 生成错误信息并将其添加到验证上下文
         */
        private void buildConstraintViolation(ObjectValidResult validateObjectResult, ConstraintValidatorContext context) {
            if (validateObjectResult.noneError()) {
                return;
            }
            context.disableDefaultConstraintViolation();
            for (FieldError error : validateObjectResult.getErrors()) {
                context.buildConstraintViolationWithTemplate(error.getErrorMessage()).addPropertyNode(error.getFieldName()).addConstraintViolation();
            }
        }

    }
}
