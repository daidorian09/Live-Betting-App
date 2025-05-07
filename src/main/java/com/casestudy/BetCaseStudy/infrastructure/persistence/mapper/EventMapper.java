package com.casestudy.BetCaseStudy.infrastructure.persistence.mapper;

import com.casestudy.BetCaseStudy.domain.model.Event;
import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;

public class EventMapper {
    public static Event toDomain(EventEntity entity) {
        return new Event(
                entity.getId(),
                entity.getLeague(),
                entity.getHomeTeam(),
                entity.getAwayTeam(),
                entity.getHomeWinRate(),
                entity.getDrawRate(),
                entity.getAwayWinRate(),
                entity.getStartTime(),
                entity.getCreatedAt()
        );
    }
}
