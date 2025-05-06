package com.casestudy.BetCaseStudy.application.service;

import com.casestudy.BetCaseStudy.application.usecase.GetLiveEventsUseCase;
import com.casestudy.BetCaseStudy.domain.model.Event;
import com.casestudy.BetCaseStudy.infrastructure.persistence.mapper.EventMapper;
import com.casestudy.BetCaseStudy.infrastructure.persistence.repository.EventJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BulletinQueryService implements GetLiveEventsUseCase {

    private final EventJpaRepository eventJpaRepository;

    @Override
    public List<Event> getLiveEvents() {
        return eventJpaRepository.findByStartTimeAfter(LocalDateTime.now())
                .stream()
                .map(EventMapper::toDomain)
                .toList();
    }
}
