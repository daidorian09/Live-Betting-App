package com.casestudy.BetCaseStudy.application.usecase;

import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;

public interface UpdateEventOddsUseCase {
    void updateEventOdds(final EventEntity eventEntity);
}
