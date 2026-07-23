package com.pizzera.catalog_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
class RedisConfig {

    @Value("${app.cache.redis.menu-ttl-minutes:5}")
    private long menuTtlMinutes;

    @Value("${app.cache.redis.locations-ttl-hours:24}")
    private long locationsTtlHours;

    @Bean
    public RedisCacheConfiguration defaultCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.java()));
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, RedisCacheConfiguration defaultCacheConfig) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        cacheConfigurations.put("menu", defaultCacheConfig.entryTtl(Duration.ofMinutes(menuTtlMinutes)));

        cacheConfigurations.put("locations", defaultCacheConfig.entryTtl(Duration.ofHours(locationsTtlHours)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}