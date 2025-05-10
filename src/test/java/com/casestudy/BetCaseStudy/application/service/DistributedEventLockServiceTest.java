package com.casestudy.BetCaseStudy.application.service;

import com.casestudy.BetCaseStudy.domain.exception.LockOperationFailedException;
import com.casestudy.BetCaseStudy.infrastructure.locking.RedisLockService;
import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.casestudy.BetCaseStudy.application.constant.ErrorMessageConstant.BET_RATE_ON_CHANGE_ERROR_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class DistributedEventLockServiceTest {

    @Mock
    private RedisLockService redisLockService;

    private DistributedEventLockService lockService;

    @BeforeEach
    void setUp() {
        lockService = new DistributedEventLockService(redisLockService);
    }

    @Test
    void shouldExecuteActionAndReturnUpdatedEntity_WhenLockAcquired() {
        // Given
        final EventEntity event = new EventEntity();
        event.setId(42L);
        final EventEntity updated = new EventEntity();
        updated.setId(42L);

        doAnswer(invocation -> {
            Runnable task = invocation.getArgument(2);
            task.run();
            return null;
        }).when(redisLockService).executeWithLock(anyString(), any(), any());

        // When
        final Optional<EventEntity> result = lockService.executeWithLock(event, () -> updated);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(updated);
    }

    @Test
    void shouldReturnEmptyOptional_WhenLockFails() {
        // Given
        final EventEntity event = new EventEntity();
        event.setId(1L);

        doThrow(new LockOperationFailedException("lock fail"))
                .when(redisLockService)
                .executeWithLock(anyString(), any(), any());

        // When
        final Optional<EventEntity> result = lockService.executeWithLock(event, EventEntity::new);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldWrapExceptionFromUpdateActionIntoRuntimeException() {
        // Given
        final EventEntity event = new EventEntity();
        event.setId(99L);

        doAnswer(invocation -> {
            Runnable task = invocation.getArgument(2);
            task.run();
            return null;
        }).when(redisLockService).executeWithLock(anyString(), any(), any());

        // When & Then
        assertThatThrownBy(() ->
                lockService.executeWithLock(event, () -> {
                    throw new IllegalStateException("exception");
                })
        ).isInstanceOf(RuntimeException.class)
                .hasMessageContaining(BET_RATE_ON_CHANGE_ERROR_MESSAGE);
    }
}
