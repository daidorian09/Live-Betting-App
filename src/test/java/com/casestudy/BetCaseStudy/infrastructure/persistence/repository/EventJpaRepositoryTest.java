package com.casestudy.BetCaseStudy.infrastructure.persistence.repository;

import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EventJpaRepositoryTest {

    @Autowired
    private EventJpaRepository eventJpaRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldReturnTrueIfEventExistsWithGivenFields() {
        // Given
        final EventEntity event = EventEntity.builder()
                .league("Super League")
                .homeTeam("Team A")
                .awayTeam("Team B")
                .startTime(LocalDateTime.of(2025, 6, 1, 20, 0))
                .build();

        eventJpaRepository.save(event);

        // When
        final boolean exists = eventJpaRepository.existsByLeagueAndHomeTeamAndAwayTeamAndStartTime(
                "Super League", "Team A", "Team B", LocalDateTime.of(2025, 6, 1, 20, 0));

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void shouldFindEventsAfterGivenTime() {
        final EventEntity event = EventEntity.builder()
                .league("Test League")
                .homeTeam("X")
                .awayTeam("Y")
                .startTime(LocalDateTime.now().plusDays(1))
                .build();

        eventJpaRepository.save(event);

        final List<EventEntity> result = eventJpaRepository.findByStartTimeAfter(LocalDateTime.now());

        assertThat(result).isNotEmpty();
    }

    @Test
    void shouldFindEventByIdSuccessfully() {
        final EventEntity event = EventEntity.builder()
                .league("Lock League")
                .homeTeam("L")
                .awayTeam("R")
                .startTime(LocalDateTime.now().plusHours(2))
                .build();

        final EventEntity saved = eventJpaRepository.save(event);

        final Optional<EventEntity> locked = eventJpaRepository.findById(saved.getId());

        assertThat(locked).isPresent();
        assertThat(locked.get().getId()).isEqualTo(saved.getId());
    }
}
