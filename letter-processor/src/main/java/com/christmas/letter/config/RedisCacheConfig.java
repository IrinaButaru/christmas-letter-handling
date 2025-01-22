package com.christmas.letter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

@Configuration
public class RedisCacheConfig {
    public static final String LETTER_CACHE_NAME = "letterCache";
    public static final String LETTER_CACHE_KEY = "#email";
//    public static final String NEW_LETTER_CACHE_KEY = "#result.email";

    @Value("${spring.cache.ttl}")
    public Long hoursTtl;

    @Bean
    public RedisCacheConfiguration cacheConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(hoursTtl))
                .disableCachingNullValues();
    }
}
