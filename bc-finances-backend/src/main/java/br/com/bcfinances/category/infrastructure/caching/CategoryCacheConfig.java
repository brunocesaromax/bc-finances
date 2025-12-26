package br.com.bcfinances.category.infrastructure.caching;

import org.springframework.boot.cache.autoconfigure.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

@Configuration
public class CategoryCacheConfig {

    @Bean
    public RedisCacheManagerBuilderCustomizer categoryCaches(RedisCacheConfiguration baseConfig) {
        return builder -> builder
                .withCacheConfiguration("categories:all", baseConfig)
                .withCacheConfiguration("categories:byId", baseConfig)
                .withCacheConfiguration("categories:byType", baseConfig);
    }
}
