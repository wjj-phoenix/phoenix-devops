package com.phoenix.devops.registrar;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 */
public class SpELValidatorBeanRegistrar implements ApplicationContextAware {

    @Getter
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext context) {
        SpELValidatorBeanRegistrar.applicationContext = context;
    }

}
