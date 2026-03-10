package com.enterprisehub.common.redis.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis缓存配置
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // 默认缓存配置
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(1))  // 默认1小时过期
            .serializeKeysWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer()))
            .disableCachingNullValues();

        // 针对不同缓存的个性化配置
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // 用户缓存：2小时过期
        cacheConfigurations.put("user", defaultConfig.entryTtl(Duration.ofHours(2)));
        
        // 菜单缓存：4小时过期
        cacheConfigurations.put("menu", defaultConfig.entryTtl(Duration.ofHours(4)));
        
        // 角色缓存：4小时过期
        cacheConfigurations.put("role", defaultConfig.entryTtl(Duration.ofHours(4)));
        
        // 部门缓存：4小时过期
        cacheConfigurations.put("dept", defaultConfig.entryTtl(Duration.ofHours(4)));

        return RedisCacheManager.builder(factory)
            .cacheDefaults(defaultConfig)
            .withInitialCacheConfigurations(cacheConfigurations)
            .transactionAware()
            .build();
    }
}
