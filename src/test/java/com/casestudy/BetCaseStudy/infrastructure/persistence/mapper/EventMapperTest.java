package com.casestudy.BetCaseStudy.infrastructure.persistence.mapper;

import com.casestudy.BetCaseStudy.domain.model.Event;
import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class EventMapperTest {
    @Test
    void shouldMapEventEntityToDomainSuccessfully() {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final EventEntity entity = EventEntity.builder()
                .id(1L)
                .league("Premier League")
                .homeTeam("Liverpool")
                .awayTeam("Manchester City")
                .homeWinRate(new BigDecimal("1.80"))
                .drawRate(new BigDecimal("3.20"))
                .awayWinRate(new BigDecimal("2.10"))
                .startTime(now.plusDays(1))
                .createdAt(now.minusDays(1))
                .updatedAt(now)
                .build();

        // when
        final Event event = EventMapper.toDomain(entity);

        // then
        assertThat(event.id()).isEqualTo(1L);
        assertThat(event.league()).isEqualTo("Premier League");
        assertThat(event.homeTeam()).isEqualTo("Liverpool");
        assertThat(event.awayTeam()).isEqualTo("Manchester City");
        assertThat(event.homeWinRate()).isEqualByComparingTo("1.80");
        assertThat(event.drawRate()).isEqualByComparingTo("3.20");
        assertThat(event.awayWinRate()).isEqualByComparingTo("2.10");
        assertThat(event.startTime()).isEqualTo(now.plusDays(1));
        assertThat(event.createdAt()).isEqualTo(now.minusDays(1));
        assertThat(event.updatedAt()).isEqualTo(now);
    }
}