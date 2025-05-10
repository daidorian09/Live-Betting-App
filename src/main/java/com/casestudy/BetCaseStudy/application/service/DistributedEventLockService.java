package com.casestudy.BetCaseStudy.application.service;

import com.casestudy.BetCaseStudy.application.usecase.EventLockManager;
import com.casestudy.BetCaseStudy.domain.exception.BetCaseStudyLockException;
import com.casestudy.BetCaseStudy.infrastructure.locking.RedisLockService;
import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static com.casestudy.BetCaseStudy.application.constant.ErrorMessageConstant.BET_RATE_ON_CHANGE_ERROR_MESSAGE;
import static com.casestudy.BetCaseStudy.application.constant.LockingConstant.LOCK_KEY;

@Service
@RequiredArgsConstructor
@Slf4j
public class DistributedEventLockService implements EventLockManager {

    private final RedisLockService redisLockService;
    private static final Duration LOCK_TIMEOUT = Duration.ofSeconds(5);

    @Override
    public Optional<EventEntity> executeWithLock(final EventEntity event, final Supplier<EventEntity> updateAction) {
        final String lockKey = "%s%d".formatted(LOCK_KEY, event.getId());

        try {
            final AtomicReference<EventEntity> updated = new AtomicReference<>();
            redisLockService.executeWithLock(lockKey, LOCK_TIMEOUT, () -> updated.set(updateAction.get()));
            return Optional.ofNullable(updated.get());
        } catch (BetCaseStudyLockException e) {
            log.warn("Lock could not be acquired : key={}", lockKey, e);
            return Optional.empty();
        } catch (Exception ex) {
            throw new RuntimeException(BET_RATE_ON_CHANGE_ERROR_MESSAGE, ex);
        }
    }
}