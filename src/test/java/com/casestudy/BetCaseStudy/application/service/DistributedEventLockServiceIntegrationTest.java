package com.casestudy.BetCaseStudy.application.service;

import com.casestudy.BetCaseStudy.domain.exception.LockOperationFailedException;
import com.casestudy.BetCaseStudy.infrastructure.locking.RedisLockService;
import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.casestudy.BetCaseStudy.application.constant.ErrorMessageConstant.BET_RATE_ON_CHANGE_ERROR_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
class DistributedEventLockServiceIntegrationTest {

    @MockBean
    private RedisLockService redisLockService;

    private DistributedEventLockService distributedLockService;

    private EventEntity sampleEvent;

    @BeforeEach
    void setUp() {
        distributedLockService = new DistributedEventLockService(redisLockService);

        sampleEvent = EventEntity.builder()
                .id(42L)
                .league("Test")
                .homeTeam("A")
                .awayTeam("B")
                .homeWinRate(BigDecimal.valueOf(2.0))
                .drawRate(BigDecimal.valueOf(3.0))
                .awayWinRate(BigDecimal.valueOf(4.0))
                .startTime(LocalDateTime.now().plusHours(2))
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldReturnUpdatedEntityWhenLockSucceeds() {
        // given
        doAnswer(invocation -> {
            Runnable action = invocation.getArgument(2);
            action.run(); // simulate successful lock
            return null;
        }).when(redisLockService).executeWithLock(any(), any(), any());

        // when
        Optional<EventEntity> result = distributedLockService.executeWithLock(sampleEvent, () -> {
            sampleEvent.setHomeWinRate(BigDecimal.valueOf(1.75));
            return sampleEvent;
        });

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getHomeWinRate()).isEqualTo(BigDecimal.valueOf(1.75));
    }

    @Test
    void shouldReturnEmptyWhenLockFails() {
        // given
        doThrow(new LockOperationFailedException("Lock failed"))
                .when(redisLockService).executeWithLock(any(), any(), any());

        // when
        Optional<EventEntity> result = distributedLockService.executeWithLock(sampleEvent, () -> sampleEvent);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldThrowRuntimeExceptionWhenUpdateFails() {
        // given
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(2);
            runnable.run();
            return null;
        }).when(redisLockService).executeWithLock(any(), any(), any());

        // when + then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                distributedLockService.executeWithLock(sampleEvent, () -> {
                    throw new IllegalStateException("Fail");
                })
        );

        assertThat(exception.getMessage()).contains(BET_RATE_ON_CHANGE_ERROR_MESSAGE);
    }
}

