package com.casestudy.BetCaseStudy.application.service;

import com.casestudy.BetCaseStudy.application.usecase.BetSlipLockManager;
import com.casestudy.BetCaseStudy.domain.exception.LockOperationFailedException;
import com.casestudy.BetCaseStudy.domain.exception.BetRateMismatchException;
import com.casestudy.BetCaseStudy.domain.exception.BetSlipTimeoutException;
import com.casestudy.BetCaseStudy.domain.exception.EventNotFoundException;
import com.casestudy.BetCaseStudy.infrastructure.locking.RedisLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class DistributedBetSlipLocker implements BetSlipLockManager {

    private final RedisLockService redisLockService;
    private static final Duration LOCK_TIMEOUT = Duration.ofSeconds(5);

    @Override
    public void executeWithLock(long eventId, String betType, Runnable action) {
        final String lockKey = String.format("lock:event:%d:betType:%s", eventId, betType);

        try {
            redisLockService.executeWithLock(lockKey, LOCK_TIMEOUT, action);
        } catch (LockOperationFailedException | BetRateMismatchException | BetSlipTimeoutException |
                 EventNotFoundException e) {
            log.warn("Lock could not be acquired : key={}, eventId={}, betType={}", lockKey, eventId, betType, e);
            throw e;
        }
    }
}
