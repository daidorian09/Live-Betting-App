package com.casestudy.BetCaseStudy.application.usecase;

import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;

import java.util.Optional;
import java.util.function.Supplier;

public interface EventLockManager {
    Optional<EventEntity> executeWithLock(final EventEntity event, final Supplier<EventEntity> updateAction);
}