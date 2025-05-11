package com.casestudy.BetCaseStudy.application.service;

import com.casestudy.BetCaseStudy.domain.model.Event;
import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import com.casestudy.BetCaseStudy.infrastructure.persistence.repository.EventJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BulletinQueryServiceIntegrationTest {

    @Autowired
    private EventJpaRepository eventJpaRepository;

    @Autowired
    private BulletinQueryService bulletinQueryService;

    @BeforeEach
    void cleanUp() {
        eventJpaRepository.deleteAll();
    }

    @Test
    void shouldReturnLiveEventsAfterNow() {
        // arrange
        eventJpaRepository.saveAll(List.of(
                EventEntity.builder()
                        .league("Test League 1")
                        .homeTeam("Team A")
                        .awayTeam("Team B")
                        .startTime(LocalDateTime.now().plusMinutes(10))
                        .homeWinRate(BigDecimal.valueOf(2.5))
                        .drawRate(BigDecimal.valueOf(3.2))
                        .awayWinRate(BigDecimal.valueOf(2.8))
                        .createdAt(LocalDateTime.now())
                        .build(),

                EventEntity.builder()
                        .league("Test League 2")
                        .homeTeam("Team C")
                        .awayTeam("Team D")
                        .startTime(LocalDateTime.now().plusHours(1))
                        .homeWinRate(BigDecimal.valueOf(1.9))
                        .drawRate(BigDecimal.valueOf(3.1))
                        .awayWinRate(BigDecimal.valueOf(3.0))
                        .createdAt(LocalDateTime.now())
                        .build()
        ));

        // act
        final List<Event> liveEvents = bulletinQueryService.getLiveEvents();

        // assert
        assertThat(liveEvents).hasSize(2);
        assertThat(liveEvents)
                .extracting(Event::league)
                .containsExactlyInAnyOrder("Test League 1", "Test League 2");
    }

    @Test
    void shouldReturnEmptyListWhenNoLiveEventsExist() {
        // arrange - already cleaned up

        // act
        final List<Event> liveEvents = bulletinQueryService.getLiveEvents();

        // assert
        assertThat(liveEvents).isEmpty();
    }
}

