package com.phoenix.devops.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 */
@Configuration
public class IRedisConfiguration {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key 采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key 采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value 采用jackson的序列化方式
        template.setValueSerializer(serializer);
        // hash的value 采用jackson的序列化方式
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}
