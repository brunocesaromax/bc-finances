package br.com.bcfinances.location.infrastructure.caching;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

@Configuration
public class LocationCacheConfig {

    @Bean
    public RedisCacheManagerBuilderCustomizer locationCaches(RedisCacheConfiguration baseConfig) {
        return builder -> builder
                .withCacheConfiguration("states:all", baseConfig)
                .withCacheConfiguration("cities:byState", baseConfig);
    }
}

