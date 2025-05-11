package com.casestudy.BetCaseStudy.application.service;

import com.casestudy.BetCaseStudy.domain.exception.BetSlipTimeoutException;
import com.casestudy.BetCaseStudy.domain.exception.LockOperationFailedException;
import com.casestudy.BetCaseStudy.infrastructure.locking.RedisLockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@SpringBootTest
class DistributedBetSlipLockerIntegrationTest {

    @MockBean
    private RedisLockService redisLockService;

    @Autowired
    private DistributedBetSlipLocker betSlipLocker;

    private final long eventId = 42L;
    private final String betType = "HOME_WIN";

    @Test
    void shouldExecuteActionWhenLockIsAcquired() {
        final AtomicBoolean executed = new AtomicBoolean(false);

        // given
        doAnswer(invocation -> {
            Runnable action = invocation.getArgument(2);
            action.run();
            return null;
        }).when(redisLockService).executeWithLock(any(), any(), any());

        // when
        betSlipLocker.executeWithLock(eventId, betType, () -> executed.set(true));

        // then
        assertThat(executed).isTrue();
        verify(redisLockService).executeWithLock(eq("lock:event:42:betType:HOME_WIN"), any(), any());
    }

    @Test
    void shouldThrowLockOperationFailedExceptionWhenLockFails() {
        // given
        doThrow(new LockOperationFailedException("lock failed"))
                .when(redisLockService).executeWithLock(any(), any(), any());

        // expect
        assertThrows(LockOperationFailedException.class, () ->
                betSlipLocker.executeWithLock(eventId, betType, () -> {})
        );

        verify(redisLockService).executeWithLock(any(), any(), any());
    }

    @Test
    void shouldLogAndPropagateCustomExceptionFromRunnable() {
        // given
        doAnswer(invocation -> {
            Runnable action = invocation.getArgument(2);
            action.run(); // simulate action failure
            return null;
        }).when(redisLockService).executeWithLock(any(), any(), any());

        // expect
        assertThrows(BetSlipTimeoutException.class, () ->
                betSlipLocker.executeWithLock(eventId, betType, () -> {
                    throw new BetSlipTimeoutException("timeout");
                })
        );

        verify(redisLockService).executeWithLock(any(), any(), any());
    }
}