package com.casestudy.BetCaseStudy.application.service;

import com.casestudy.BetCaseStudy.domain.exception.BetRateMismatchException;
import com.casestudy.BetCaseStudy.domain.exception.BetSlipTimeoutException;
import com.casestudy.BetCaseStudy.domain.exception.EventNotFoundException;
import com.casestudy.BetCaseStudy.domain.exception.LockOperationFailedException;
import com.casestudy.BetCaseStudy.infrastructure.locking.RedisLockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DistributedBetSlipLockerTest {

    @Mock
    private RedisLockService redisLockService;

    private DistributedBetSlipLocker betSlipLocker;

    @BeforeEach
    void setUp() {
        betSlipLocker = new DistributedBetSlipLocker(redisLockService);
    }

    @Test
    void shouldExecuteActionWhenLockIsAcquired() {
        // Arrange
        final Runnable action = mock(Runnable.class);

        // Act
        betSlipLocker.executeWithLock(1L, "HOME_WIN", action);

        // Assert
        verify(redisLockService).executeWithLock(eq("lock:event:1:betType:HOME_WIN"), any(), eq(action));
    }

    @ParameterizedTest
    @MethodSource("rethrowableExceptions")
    void shouldRethrowKnownExceptions(RuntimeException exception) {
        // Arrange
        Runnable action = mock(Runnable.class);
        doThrow(exception).when(redisLockService).executeWithLock(anyString(), any(), any());

        // Act & Assert
        assertThatThrownBy(() -> betSlipLocker.executeWithLock(42L, "DRAW", action))
                .isSameAs(exception);
    }

    private static Stream<RuntimeException> rethrowableExceptions() {
        return Stream.of(
                new LockOperationFailedException("Lock failed"),
                new BetRateMismatchException("Rate mismatch"),
                new BetSlipTimeoutException("Timeout"),
                new EventNotFoundException("Event not found")
        );
    }
}