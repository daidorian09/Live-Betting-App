package com.casestudy.BetCaseStudy.application.service;

import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class EventOddsUpdaterServiceTest {

    private EventOddsUpdaterService oddsUpdaterService;

    @BeforeEach
    void setUp() {
        oddsUpdaterService = new EventOddsUpdaterService();
    }

    @Test
    void shouldUpdateAllEventOddsWithRandomFactor() {
        // Arrange
        EventEntity event = new EventEntity();
        event.setHomeWinRate(BigDecimal.valueOf(2.00));
        event.setDrawRate(BigDecimal.valueOf(3.00));
        event.setAwayWinRate(BigDecimal.valueOf(4.00));

        // Act
        oddsUpdaterService.updateEventOdds(event);

        // Assert
        assertThat(event.getHomeWinRate()).isNotEqualByComparingTo("2.00");
        assertThat(event.getDrawRate()).isNotEqualByComparingTo("3.00");
        assertThat(event.getAwayWinRate()).isNotEqualByComparingTo("4.00");

        assertThat(event.getHomeWinRate().scale()).isEqualTo(2);
        assertThat(event.getDrawRate().scale()).isEqualTo(2);
        assertThat(event.getAwayWinRate().scale()).isEqualTo(2);

        assertIsInExpectedRange(event.getHomeWinRate(), BigDecimal.valueOf(2.00));
        assertIsInExpectedRange(event.getDrawRate(), BigDecimal.valueOf(3.00));
        assertIsInExpectedRange(event.getAwayWinRate(), BigDecimal.valueOf(4.00));
    }

    private void assertIsInExpectedRange(BigDecimal updated, BigDecimal original) {
        BigDecimal min = original.multiply(BigDecimal.valueOf(EventOddsUpdaterService.MIN_FACTOR));
        BigDecimal max = original.multiply(BigDecimal.valueOf(EventOddsUpdaterService.MAX_FACTOR));

        assertThat(updated).isBetween(min, max);
    }
}
