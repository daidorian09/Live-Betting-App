package com.casestudy.BetCaseStudy.domain.model;

import com.casestudy.BetCaseStudy.domain.exception.InvalidBetTypeException;
import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BetTypeTest {

    @Test
    void shouldReturnCorrectEnumFromString() {
        assertThat(BetType.fromString("home_win")).isEqualTo(BetType.HOME_WIN);
        assertThat(BetType.fromString("DRAW")).isEqualTo(BetType.DRAW);
        assertThat(BetType.fromString("Away_Win")).isEqualTo(BetType.AWAY_WIN);
    }

    @Test
    void shouldThrowExceptionForInvalidBetType() {
        assertThatThrownBy(() -> BetType.fromString("invalid_type"))
                .isInstanceOf(InvalidBetTypeException.class)
                .hasMessageContaining("Invalid bet type");
    }

    @Test
    void shouldReturnCorrectRateForEachBetType() {
        final EventEntity event = EventEntity.builder()
                .homeWinRate(new BigDecimal("1.50"))
                .drawRate(new BigDecimal("3.20"))
                .awayWinRate(new BigDecimal("2.80"))
                .build();

        assertThat(BetType.HOME_WIN.getRateFrom(event)).isEqualByComparingTo("1.50");
        assertThat(BetType.DRAW.getRateFrom(event)).isEqualByComparingTo("3.20");
        assertThat(BetType.AWAY_WIN.getRateFrom(event)).isEqualByComparingTo("2.80");
    }
}