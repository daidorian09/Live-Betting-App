package com.casestudy.BetCaseStudy.application.usecase;

import com.casestudy.BetCaseStudy.application.dto.EventRequest;

public interface CreateEventUseCase {
    void create(final EventRequest request);
}
