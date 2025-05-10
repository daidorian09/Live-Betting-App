package com.casestudy.BetCaseStudy.infrastructure.locking;

import com.casestudy.BetCaseStudy.domain.exception.BetCaseStudyLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisLockServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOps;

    private RedisLockService redisLockService;

    @BeforeEach
    void setUp() {
        redisLockService = new RedisLockService(redisTemplate);
    }

    @Test
    void shouldExecuteTaskWhenLockIsAcquired() {
        // given
        final String key = "lock:test";
        final Duration timeout = Duration.ofSeconds(5);
        final AtomicBoolean executed = new AtomicBoolean(false);

        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.setIfAbsent(eq(key), anyString(), eq(timeout))).thenReturn(true);

        // when
        redisLockService.executeWithLock(key, timeout, () -> executed.set(true));

        // then
        assertThat(executed.get()).isTrue();
        verify(redisTemplate).execute(any(), eq(List.of(key)), any());
    }

    @Test
    void shouldThrowExceptionWhenLockIsNotAcquired() {
        // given
        final String key = "lock:test";
        final Duration timeout = Duration.ofSeconds(5);

        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.setIfAbsent(eq(key), anyString(), eq(timeout))).thenReturn(false);

        // when / then
        assertThatThrownBy(() ->
                redisLockService.executeWithLock(key, timeout, () -> {})
        ).isInstanceOf(BetCaseStudyLockException.class)
                .hasMessageContaining("Could not acquire Redis lock for key");

        verify(redisTemplate, never()).execute(any(), anyList(), any());
    }

    @Test
    void shouldReleaseLockEvenIfTaskThrows() {
        // given
        final String key = "lock:test";
        final Duration timeout = Duration.ofSeconds(5);

        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.setIfAbsent(eq(key), anyString(), eq(timeout))).thenReturn(true);

        // when / then
        assertThatThrownBy(() ->
                redisLockService.executeWithLock(key, timeout, () -> {
                    throw new RuntimeException("boom");
                })
        ).isInstanceOf(RuntimeException.class)
                .hasMessage("boom");

        // lock is still released
        verify(redisTemplate).execute(any(), eq(List.of(key)), any());
    }
}
