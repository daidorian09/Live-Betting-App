package com.casestudy.BetCaseStudy.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EventEntityTest {

    @Test
    void shouldSetCreatedAtOnPrePersist() {
        // given
        final EventEntity event = EventEntity.builder()
                .league("Super League")
                .homeTeam("Team A")
                .awayTeam("Team B")
                .homeWinRate(BigDecimal.valueOf(2.1))
                .drawRate(BigDecimal.valueOf(3.5))
                .awayWinRate(BigDecimal.valueOf(1.8))
                .startTime(LocalDateTime.now().plusDays(1))
                .build();

        // when
        event.prePersist();

        // then
        assertThat(event.getCreatedAt()).isNotNull();
        assertThat(event.getUpdatedAt()).isNull();
    }

    @Test
    void shouldSetUpdatedAtOnPreUpdate() {
        // given
        final EventEntity event = EventEntity.builder().build();

        // when
        event.preUpdate();

        // then
        assertThat(event.getUpdatedAt()).isNotNull();
    }
}