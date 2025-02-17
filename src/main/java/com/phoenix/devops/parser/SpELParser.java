package com.phoenix.devops.parser;

import com.phoenix.devops.exception.SpELParserException;
import com.phoenix.devops.registrar.SpELValidatorBeanRegistrar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wjj-phoenix
 * @since 2025-02-17\
 * SpEL 表达式解析工具
 */
@Slf4j
public class SpELParser {
    private SpELParser() {
    }

    private static final SpelExpressionParser parser = new SpelExpressionParser();

    private static final StandardEvaluationContext context = new StandardEvaluationContext();

    private static final Map<String, Expression> expressionCache = new ConcurrentHashMap<>();

    // 由于init当中使用了静态变量，故此静态代码块必须放在静态变量的定义之后，否则会出现空指针异常。
    static {
        init();
    }

    private static void init() {
        ApplicationContext applicationContext = SpELValidatorBeanRegistrar.getApplicationContext();
        if (applicationContext != null) {
            AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
            context.setBeanResolver(new BeanFactoryResolver(beanFactory));
        } else {
            log.info("ApplicationContext is null, SpelParser will not support spring bean reference");
            log.info("If you want to use spring bean reference in SpelParser, please use @EnableSpelValidatorBeanRegistrar to enable ApplicationContext support");
        }
        log.debug("SpelParser init success");
    }

    /**
     * 解析表达式
     *
     * @param expression 表达式
     * @param rootObject 用于计算表达式的根对象
     * @return 表达式计算结果。若为基本数据类型，则会自动转为包装类型。
     */
    public static Object parse(String expression, Object rootObject) {
        try {
            log.debug("======> Parse expression [{}]", expression);
            Expression parsed = expressionCache.computeIfAbsent(expression, parser::parseExpression);
            Object value = parsed.getValue(context, rootObject, Object.class);
            log.debug("======> Parse result [{}]", value);
            return value;
        } catch (RuntimeException e) {
            throw new SpELParserException("Parse expression error, expression [" + expression + "], message [" + e.getMessage() + "]", e);
        }
    }

    /**
     * 解析表达式
     *
     * @param <T>          返回值类型
     * @param expression   表达式
     * @param rootObject   用于计算表达式的根对象
     * @param requiredType 指定返回值的类型
     * @return 表达式计算结果。若为基本数据类型，则会自动转为包装类型。
     * @throws SpELParserException 当表达式计算结果为null或者不是指定类型时抛出
     */
    public static <T> T parse(String expression, Object rootObject, Class<T> requiredType) {
        Object any = parse(expression, rootObject);
        if (any == null) {
            throw new SpELParserException("Expression [" + expression + "] calculate result can not be null");
        }
        if (!requiredType.isInstance(any)) {
            throw new SpELParserException("Expression [" + expression + "] calculate result must be [" + requiredType.getName() + "]");
        }
        // noinspection unchecked
        return (T) any;
    }
}
