package com.casestudy.BetCaseStudy.application.service;

import com.casestudy.BetCaseStudy.application.usecase.UpdateEventOddsUseCase;
import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EventOddsUpdaterService implements UpdateEventOddsUseCase {
    public static final double MIN_FACTOR = 0.95;
    public static final double MAX_FACTOR = 1.05;
    public static final int SCALE = 2;
    private final Random random = new Random();

    @Override
    @Transactional
    public void updateEventOdds(final EventEntity eventEntity) {
        eventEntity.setHomeWinRate(randomize(eventEntity.getHomeWinRate()));
        eventEntity.setDrawRate(randomize(eventEntity.getDrawRate()));
        eventEntity.setAwayWinRate(randomize(eventEntity.getAwayWinRate()));
    }

    private BigDecimal randomize(BigDecimal value) {
        double factor = MIN_FACTOR + (MAX_FACTOR - MIN_FACTOR) * random.nextDouble();
        return value.multiply(BigDecimal.valueOf(factor)).setScale(SCALE, RoundingMode.HALF_UP);
    }
}
