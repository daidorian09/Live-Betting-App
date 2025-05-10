package com.casestudy.BetCaseStudy.configuration;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;

@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "PT20S")
public class SchedulerLockConfig {

    @Value("${shedlock.redis.key-prefix}")
    private String shedlockKeyPrefix;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password:}")
    private String redisPassword;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);

        if (StringUtils.isNotBlank(redisPassword)) {
            config.setPassword(RedisPassword.of(redisPassword));
        }

        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisLockProvider redisLockProvider(final RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockProvider(redisConnectionFactory, shedlockKeyPrefix);
    }
}

