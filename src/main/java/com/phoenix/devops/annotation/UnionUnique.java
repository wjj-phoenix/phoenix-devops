package com.phoenix.devops.annotation;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import jakarta.annotation.Resource;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 * 字段组合在一个表中唯一
 */
@Documented
@Target({ElementType.TYPE})
// 允许重复注解
@Repeatable(UnionUnique.List.class)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UnionUnique.UnionUniqueValidator.class})
public @interface UnionUnique {
    /**
     * 查询数据库的服务接口
     *
     * @return 服务接口
     */
    Class<? extends IService<?>> service();

    /**
     * 组合字段
     *
     * @return 组合字段名称
     */
    String[] fields();

    /**
     * 错误提示信息
     *
     * @return 错误提示信息
     */
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Documented
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        UnionUnique[] value();
    }

    class UnionUniqueValidator implements ConstraintValidator<UnionUnique, Object> {
        private UnionUnique unionUnique;

        @Resource
        private ApplicationContext applicationContext;

        @Override
        public void initialize(UnionUnique unionUnique) {
            this.unionUnique = unionUnique;
        }

        @Override
        public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
            String[] fields = unionUnique.fields();
            StringBuilder wrapperSql = new StringBuilder("1=1");
            java.util.List<Object> vals = new ArrayList<>();

            for (String fieldStr : fields) {
                // 使用反射机制获取字段对象
                Field field = ReflectionUtils.findField(obj.getClass(), fieldStr);
                Assert.notNull(field, "field " + fieldStr + " not found");
                ReflectionUtils.makeAccessible(field);

                // 拼接sql，值使用占位符方式，防止SQL注入
                wrapperSql.append(String.format(" and %s=? ", field.getName()));

                // 获取字段值
                vals.add(ReflectionUtils.getField(field, obj));
            }

            IService<?> service = applicationContext.getBean(unionUnique.service());
            // 调用存储库方法来检查唯一性（需要根据具体情况实现）
            return !service.exists(QueryWrapper.create().where(wrapperSql.toString(), vals.toArray()));
        }
    }
}
