package com.phoenix.devops.executor;

import com.phoenix.devops.annotation.SpELConstraint;
import com.phoenix.devops.exception.SpELNotSupportedTypeException;
import com.phoenix.devops.exception.SpELValidatorException;
import com.phoenix.devops.lang.FieldValidResult;
import com.phoenix.devops.lang.ObjectValidResult;
import com.phoenix.devops.manager.AnnotationMethodManager;
import com.phoenix.devops.manager.ValidatorInstanceManager;
import com.phoenix.devops.parser.SpELParser;
import com.phoenix.devops.validator.SpELConstraintValidator;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 */
@Slf4j
public class SpELValidExecutor {
    private SpELValidExecutor() {
    }

    private static final String MESSAGE = "message";

    private static final String CONDITION = "condition";

    private static final String GROUP = "group";

    /**
     * 字段缓存，仅缓存包含 SpEL 约束注解的字段
     */
    private static final ConcurrentHashMap<Class<?>, List<Field>> FIELD_CACHE = new ConcurrentHashMap<>();

    /**
     * 字段注解缓存
     */
    private static final ConcurrentHashMap<Field, List<Annotation>> FIELD_ANNOTATION_CACHE = new ConcurrentHashMap<>();

    /**
     * 验证对象
     * <p>
     * 如果对象中有任意使用了 SpEL 约束注解的字段，则会对该字段进行校验。
     *
     * @param verifiedObject 被校验的对象
     * @return 对象校验结果
     */
    @NotNull
    public static ObjectValidResult validateObject(@NotNull Object verifiedObject) {
        return validateObject(verifiedObject, Collections.emptySet());
    }

    /**
     * 验证对象
     * <p>
     * 如果对象中有任意使用了 SpEL 约束注解的字段，则会对该字段进行校验。
     *
     * @param verifiedObject 被校验的对象
     * @param validateGroups 分组信息，只有同组的注解才会被校验
     * @return 对象校验结果
     */
    @NotNull
    public static ObjectValidResult validateObject(@NotNull Object verifiedObject, String[] validateGroups) {
        if (validateGroups == null) {
            return validateObject(verifiedObject);
        }
        // 获取分组信息
        Set<Object> groups = Arrays.stream(validateGroups).map(it -> SpELParser.parse(it, verifiedObject)).collect(Collectors.toSet());
        return validateObject(verifiedObject, groups);
    }

    /**
     * 验证对象
     * <p>
     * 如果对象中有任意使用了 SpEL 约束注解的字段，则会对该字段进行校验。
     *
     * @param verifiedObject 被校验的对象
     * @param validateGroups 分组信息，只有同组的注解才会被校验
     * @return 对象校验结果
     */
    @NotNull
    public static ObjectValidResult validateObject(@NotNull Object verifiedObject, @NotNull Set<Object> validateGroups) {
        Objects.requireNonNull(verifiedObject);
        Objects.requireNonNull(validateGroups);

        long startTime = System.nanoTime();
        log.debug("Spel validate start, class [{}], groups [{}]", verifiedObject.getClass().getName(), validateGroups);
        log.debug("Verified object [{}]", verifiedObject);

        ObjectValidResult validResult = new ObjectValidResult();

        // 获取字段
        List<Field> spelConstraintFields = getSpELConstraintFields(verifiedObject.getClass());
        for (Field field : spelConstraintFields) {
            // 获取注解
            List<Annotation> spelConstraintAnnotations = getSpELConstraintAnnotations(field);
            for (Annotation annotation : spelConstraintAnnotations) {
                // 获取验证器实例
                SpELConstraintValidator<? extends Annotation> validator = ValidatorInstanceManager.getInstance(annotation);
                // 执行校验
                FieldValidResult validationResult = validateFieldAnnotation(annotation, validator, verifiedObject, field, validateGroups);
                if (validationResult != null) {
                    validResult.addFieldResult(validationResult);
                }
            }
        }

        log.debug("Spel validate over,error list {}", validResult.getErrors());
        log.debug("Spel validate cost time {} ms", (System.nanoTime() - startTime) / 1000000);
        return validResult;
    }

    /**
     * 获取包含 SpEL 约束注解的字段列表
     */
    @NotNull
    private static List<Field> getSpELConstraintFields(@NotNull Class<?> clazz) {
        return FIELD_CACHE.computeIfAbsent(clazz, aClass -> {
            List<Field> list = new ArrayList<>();
            while (aClass != null) {
                Field[] fields = aClass.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (!getSpELConstraintAnnotations(field).isEmpty()) {
                        list.add(field);
                    }
                }
                // 获取父类，继续处理
                aClass = aClass.getSuperclass();
            }
            return Collections.unmodifiableList(list);
        });
    }

    /**
     * 获取字段上符合 SpEL 约束规范 的注解
     *
     * @param field 字段
     * @return 注解列表
     */
    @NotNull
    private static List<Annotation> getSpELConstraintAnnotations(@NotNull Field field) {
        return FIELD_ANNOTATION_CACHE.computeIfAbsent(field, f -> {
            Annotation[] annotations = f.getAnnotations();
            List<Annotation> tempList = new ArrayList<>();

            for (Annotation originalAnno : annotations) {
                String annoName = originalAnno.annotationType().getName();
                if (annoName.endsWith("$List") || annoName.endsWith("Container")) {
                    // 容器注解，需要获取容器内部的注解类型，其声明类为真实的注解类
                    //noinspection unchecked
                    Class<? extends Annotation> clazz = (Class<? extends Annotation>) originalAnno.annotationType().getDeclaringClass();
                    Annotation[] originalAnnoArray = f.getAnnotationsByType(clazz);
                    tempList.addAll(Arrays.asList(originalAnnoArray));
                } else {
                    tempList.add(originalAnno);
                }
            }

            // 验证注解的合法性，移除不合法的注解
            tempList.removeIf(annotation -> !isSpelConstraintAnnotation(annotation.annotationType()));

            return Collections.unmodifiableList(tempList);
        });
    }

    /**
     * 对任意的注解执行校验，当注解不是合法的约束注解时，将返回null。
     *
     * @param annotation        注解数组，数组内的注解必须为同一类型
     * @param verifiedObject    被校验的对象
     * @param verifiedField     被校验的字段，必须存在于被校验的对象中
     * @param validateGroups    分组信息
     * @return 校验结果
     */
    private static @Nullable FieldValidResult validateFieldAnnotation(
            @NotNull Annotation annotation,
            @NotNull SpELConstraintValidator<? extends Annotation> validator,
            @NotNull Object verifiedObject,
            @NotNull Field verifiedField,
            @NotNull Set<Object> validateGroups
    ) {
        log.debug("===> Find target annotation [{}], verifiedField [{}]", annotation.annotationType().getSimpleName(), verifiedField.getName());
        log.debug("===> Annotation object [{}]", annotation);

        // 判断字段的类型是否受支持
        Set<Class<?>> supported = validator.supportType();
        Class<?> verifiedFieldClass = verifiedField.getType();
        if (supported.stream().noneMatch(clazz -> clazz.isAssignableFrom(verifiedFieldClass))) {
            log.error("===> Object type not supported, skip validate. Current type[{}], supported types [{}]", verifiedFieldClass, supported);
            throw new SpELNotSupportedTypeException(verifiedFieldClass, supported);
        }

        // 匹配分组
        Set<Object> annoGroups = parseGroups(annotation, verifiedObject);
        if (!annoGroups.isEmpty() && !matchGroup(validateGroups, annoGroups)) {
            log.debug("===> Group not matched, skip validate. annotation groups [{}]", annoGroups);
            return null;
        }

        // 判断condition条件是否成立
        String condition = getAnnotationValue(annotation, CONDITION);
        if (!condition.isEmpty() && !SpELParser.parse(condition, verifiedObject, Boolean.class)) {
            log.debug("===> Condition not valid, skip validate. condition [{}]", condition);
            return null;
        }

        // 执行校验
        FieldValidResult validationResult = doValidate(validator, annotation, verifiedObject, verifiedField);
        log.debug("===> Validate result [{}]", validationResult.isSuccess());
        return validationResult;
    }

    /**
     * 执行校验
     *
     * @param validator      校验器
     * @param annotation     注解
     * @param verifiedObject 被校验的对象
     * @param verifiedField  被校验的字段，必须存在于被校验的对象中
     */
    @NotNull
    private static <A extends Annotation> FieldValidResult doValidate(
            @NotNull SpELConstraintValidator<?> validator,
            @NotNull A annotation,
            @NotNull Object verifiedObject,
            @NotNull Field verifiedField
    ) {
        FieldValidResult result;
        try {
            // noinspection unchecked
            result = ((SpELConstraintValidator<A>) validator).isValid(annotation, verifiedObject, verifiedField);
        } catch (SpELValidatorException e) {
            log.error("Spel validate error: {}; Located in the annotation [{}] of class [{}] field [{}]",
                    e.getMessage(), annotation.annotationType().getName(), verifiedObject.getClass().getName(), verifiedField.getName());
            throw e;
        } catch (IllegalAccessException e) {
            // 被验证的字段在类中无法访问
            log.error("The validated field [{}] is not accessible in the class [{}]",
                    verifiedField.getName(), verifiedObject.getClass().getName());
            throw new SpELValidatorException("Failed to access field value", e);
        }
        autoFillValidResult(result, annotation, verifiedField);
        return result;
    }

    /**
     * 判断注解是否为合法的约束注解
     */
    private static boolean isSpelConstraintAnnotation(@NotNull Class<? extends Annotation> annotationType) {
        if (!annotationType.isAnnotationPresent(SpELConstraint.class)) {
            return false;
        }

        if (AnnotationMethodManager.get(annotationType, MESSAGE) == null) {
            log.warn("The annotation [{}] must have a method named [message] that returns a string.", annotationType.getName());
            return false;
        }

        if (AnnotationMethodManager.get(annotationType, CONDITION) == null) {
            log.warn("The annotation [{}] must have a method named [condition] that returns a string.", annotationType.getName());
            return false;
        }

        if (AnnotationMethodManager.get(annotationType, GROUP) == null) {
            log.warn("The annotation [{}] must have a method named [group] that returns a Array<String>.", annotationType.getName());
            return false;
        }

        return true;
    }

    /**
     * 判断组别是否匹配
     */
    @SuppressWarnings("RedundantIfStatement")
    private static boolean matchGroup(@NotNull Set<Object> validateGroups, @NotNull Set<Object> annoGroups) {
        if (validateGroups.isEmpty()) {
            return true;
        }
        if (validateGroups.stream().anyMatch(annoGroups::contains)) {
            return true;
        }
        return false;
    }

    /**
     * 解析组别信息
     */
    @NotNull
    private static Set<Object> parseGroups(@NotNull Annotation annotation, @NotNull Object value) {
        String[] groups = getAnnotationValue(annotation, GROUP);
        Set<Object> parsedGroups = new HashSet<>();
        for (String group : groups) {
            parsedGroups.add(SpELParser.parse(group, value));
        }
        return parsedGroups;
    }

    /**
     * 自动填充校验结果的错误信息
     */
    private static void autoFillValidResult(@NotNull FieldValidResult result, @NotNull Annotation annotation, @NotNull Field verifiedField) {
        if (!result.isSuccess()) {
            // 错误信息收集
            if (result.getMessage().isEmpty()) {
                String message = getAnnotationValue(annotation, MESSAGE);
                result.setMessage(message);
            }
            if (result.getFieldName().isEmpty()) {
                result.setFieldName(verifiedField.getName());
            }
        }
    }

    /**
     * 获取注解方法的属性值
     *
     * @param annotation 注解对象
     * @param methodName 方法名
     * @param <T>        返回值类型
     * @return 属性值
     */
    private static <T> T getAnnotationValue(@NotNull Annotation annotation, @NotNull String methodName) {
        Method method = AnnotationMethodManager.get(annotation.annotationType(), methodName);
        try {
            //noinspection unchecked
            return (T) method.invoke(annotation);
        } catch (Exception e) {
            throw new SpELValidatorException("Get method [" + annotation.annotationType().getName() + "." + methodName + "] error: " + e.getMessage(), e);
        }
    }

}
