package com.casestudy.BetCaseStudy.application.usecase;

import com.casestudy.BetCaseStudy.application.dto.EventRequest;
import com.casestudy.BetCaseStudy.domain.model.Event;

public interface CreateEventUseCase {
    void create(final EventRequest request);
}
