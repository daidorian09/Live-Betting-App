package com.casestudy.BetCaseStudy.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SchedulerLockConfigTest {

    @Test
    void shouldCreateBeansWithPassword() {
        // given
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        TestPropertyValues.of(
                "shedlock.redis.key-prefix=shedlock:",
                "spring.data.redis.host=localhost",
                "spring.data.redis.port=6379",
                "spring.data.redis.password=secret"
        ).applyTo(context);
        context.register(SchedulerLockConfig.class);
        context.refresh();

        // when
        RedisConnectionFactory factory = context.getBean(RedisConnectionFactory.class);
        RedisLockProvider provider = context.getBean(RedisLockProvider.class);
        StringRedisTemplate template = context.getBean(StringRedisTemplate.class);

        // then
        assertThat(factory).isNotNull();
        assertThat(provider).isNotNull();
        assertThat(template).isNotNull();

        context.close();
    }

    @Test
    void shouldCreateBeansWithoutPassword() {
        // given
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        TestPropertyValues.of(
                "shedlock.redis.key-prefix=shedlock:",
                "spring.data.redis.host=localhost",
                "spring.data.redis.port=6379"
                // no password property
        ).applyTo(context);
        context.register(SchedulerLockConfig.class);
        context.refresh();

        // when
        RedisConnectionFactory factory = context.getBean(RedisConnectionFactory.class);
        RedisLockProvider provider = context.getBean(RedisLockProvider.class);
        StringRedisTemplate template = context.getBean(StringRedisTemplate.class);

        // then
        assertThat(factory).isNotNull();
        assertThat(provider).isNotNull();
        assertThat(template).isNotNull();

        context.close();
    }
}