package com.casestudy.BetCaseStudy.application.service;

import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import com.casestudy.BetCaseStudy.infrastructure.persistence.repository.EventJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EventOddsUpdaterServiceIntegrationTest {

    @Autowired
    private EventOddsUpdaterService eventOddsUpdaterService;

    @Autowired
    private EventJpaRepository eventJpaRepository;

    private EventEntity event;

    @BeforeEach
    void setUp() {
        eventJpaRepository.deleteAll();

        event = EventEntity.builder()
                .league("Test League")
                .homeTeam("Team A")
                .awayTeam("Team B")
                .homeWinRate(BigDecimal.valueOf(2.00))
                .drawRate(BigDecimal.valueOf(3.00))
                .awayWinRate(BigDecimal.valueOf(4.00))
                .startTime(LocalDateTime.now().plusHours(2))
                .createdAt(LocalDateTime.now())
                .build();

        event = eventJpaRepository.save(event);
    }

    @Test
    void shouldUpdateEventOddsWithinExpectedRange() {
        // WHEN
        eventOddsUpdaterService.updateEventOdds(event);

        // THEN
        final BigDecimal minHome = BigDecimal.valueOf(2.00 * 0.95).setScale(2, RoundingMode.HALF_UP);
        final BigDecimal maxHome = BigDecimal.valueOf(2.00 * 1.05).setScale(2, RoundingMode.HALF_UP);
        final BigDecimal updatedHome = event.getHomeWinRate();

        assertThat(updatedHome).isBetween(minHome, maxHome);

        final BigDecimal updatedDraw = event.getDrawRate();
        final BigDecimal minDraw = BigDecimal.valueOf(3.00 * 0.95).setScale(2, RoundingMode.HALF_UP);
        final  BigDecimal maxDraw = BigDecimal.valueOf(3.00 * 1.05).setScale(2, RoundingMode.HALF_UP);
        assertThat(updatedDraw).isBetween(minDraw, maxDraw);

        final BigDecimal updatedAway = event.getAwayWinRate();
        final  BigDecimal minAway = BigDecimal.valueOf(4.00 * 0.95).setScale(2, RoundingMode.HALF_UP);
        final BigDecimal maxAway = BigDecimal.valueOf(4.00 * 1.05).setScale(2, RoundingMode.HALF_UP);
        assertThat(updatedAway).isBetween(minAway, maxAway);
    }
}
