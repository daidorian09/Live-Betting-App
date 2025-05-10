package com.casestudy.BetCaseStudy.infrastructure.persistence.repository;

import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

public interface EventJpaRepository extends JpaRepository<EventEntity, Long> {
    boolean existsByLeagueAndHomeTeamAndAwayTeamAndStartTime(final String league, final String homeTeam, final String awayTeam, final LocalDateTime startTime);

    List<EventEntity> findByStartTimeAfter(final LocalDateTime dateTime);

    Page<EventEntity> findAllByStartTimeAfter(final LocalDateTime dateTime, final Pageable pageable);

    Optional<EventEntity> findById(final long id);
}