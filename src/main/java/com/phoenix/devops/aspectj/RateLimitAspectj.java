package com.phoenix.devops.aspectj;

import com.google.common.util.concurrent.RateLimiter;
import com.phoenix.devops.annotation.RateLimit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 */
@Slf4j
@Aspect
@Component
public class RateLimitAspectj {

    private final ConcurrentHashMap<String, RateLimiter> EXISTED_RATE_LIMITERS = new ConcurrentHashMap<>();

    @Pointcut("@annotation(com.phoenix.devops.annotation.RateLimit)")
    public void rateLimit() {
    }

    @Around("rateLimit()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        RateLimit annotation = AnnotationUtils.findAnnotation(method, RateLimit.class);

        // get rate limiter
        RateLimiter rateLimiter = EXISTED_RATE_LIMITERS.computeIfAbsent(method.getName(), k -> RateLimiter.create(annotation.limit()));

        // process
        if (rateLimiter!=null && rateLimiter.tryAcquire()) {
            return point.proceed();
        } else {
            throw new RuntimeException("too many requests, please try again later...");
        }
    }
}