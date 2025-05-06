package com.casestudy.BetCaseStudy.application.usecase;

import com.casestudy.BetCaseStudy.domain.model.Event;

import java.util.List;

public interface GetLiveEventsUseCase {
    List<Event> getLiveEvents();
}
