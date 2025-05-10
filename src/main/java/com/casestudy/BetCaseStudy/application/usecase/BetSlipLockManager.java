package com.casestudy.BetCaseStudy.application.usecase;

public interface BetSlipLockManager {
    void executeWithLock(final long eventId, final String betType, final Runnable action);
}

