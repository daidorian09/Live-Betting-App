package com.casestudy.BetCaseStudy.configuration;


import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;

@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "PT10S")
public class SchedulerLockConfig {

    @Value("${shedlock.redis.key-prefix:shedlock}")
    private String shedlockKeyPrefix;

    @Value("${spring.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.redis.port:6379}")
    private int redisPort;

    @Value("${spring.redis.password:}")
    private String redisPassword;

    /*@Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        if (redisPassword != null && !redisPassword.isBlank()) {
            config.setPassword(redisPassword);
        }
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisLockProvider redisLockProvider(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockProvider(redisConnectionFactory, shedlockKeyPrefix);
    }*/
}
