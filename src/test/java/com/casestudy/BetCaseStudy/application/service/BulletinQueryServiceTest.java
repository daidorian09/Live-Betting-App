package com.casestudy.BetCaseStudy.application.service;

import com.casestudy.BetCaseStudy.domain.model.Event;
import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import com.casestudy.BetCaseStudy.infrastructure.persistence.repository.EventJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BulletinQueryServiceTest {

    @Mock
    private EventJpaRepository eventJpaRepository;

    private BulletinQueryService bulletinQueryService;

    @BeforeEach
    void setUp() {
        bulletinQueryService = new BulletinQueryService(eventJpaRepository);
    }

    @Test
    void shouldReturnMappedEvents_WhenEventsExist() {
        // Given
        final EventEntity entity = EventEntity.builder()
                .id(1L)
                .league("Süper Lig")
                .homeTeam("Galatasaray")
                .awayTeam("Fenerbahçe")
                .homeWinRate(new BigDecimal("1.90"))
                .drawRate(new BigDecimal("3.20"))
                .awayWinRate(new BigDecimal("2.50"))
                .startTime(LocalDateTime.now().plusHours(1))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(eventJpaRepository.findByStartTimeAfter(any()))
                .thenReturn(Collections.singletonList(entity));

        // When
        final List<Event> events = bulletinQueryService.getLiveEvents();

        // Then
        assertThat(events).hasSize(1);
        Event event = events.get(0);
        assertThat(event.id()).isEqualTo(1L);
        assertThat(event.league()).isEqualTo("Süper Lig");
        assertThat(event.homeTeam()).isEqualTo("Galatasaray");
        assertThat(event.awayTeam()).isEqualTo("Fenerbahçe");
    }

    @Test
    void shouldReturnEmptyList_WhenNoEventsExist() {
        // Given
        when(eventJpaRepository.findByStartTimeAfter(any())).thenReturn(List.of());

        // When
        List<Event> events = bulletinQueryService.getLiveEvents();

        // Then
        assertThat(events).isEmpty();
    }
}