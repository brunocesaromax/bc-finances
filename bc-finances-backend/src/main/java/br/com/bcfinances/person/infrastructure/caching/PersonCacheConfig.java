package br.com.bcfinances.person.infrastructure.caching;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

@Configuration
public class PersonCacheConfig {

    @Bean
    public RedisCacheManagerBuilderCustomizer personCaches(RedisCacheConfiguration baseConfig) {
        return builder -> builder
                .withCacheConfiguration("persons:byId", baseConfig.entryTtl(Duration.ofMinutes(10)))
                .withCacheConfiguration("persons:all", baseConfig.entryTtl(Duration.ofMinutes(5)))
                .withCacheConfiguration("persons:search", baseConfig.entryTtl(Duration.ofMinutes(2)));
    }
}

