package com.casestudy.BetCaseStudy.infrastructure.persistence.repository;

import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

public interface EventJpaRepository extends JpaRepository<EventEntity, Long> {
    boolean existsByLeagueAndHomeTeamAndAwayTeamAndStartTime(final String league, final String homeTeam, final String awayTeam, final LocalDateTime startTime);

    List<EventEntity> findByStartTimeAfter(final LocalDateTime dateTime);

    Page<EventEntity> findByStartTimeAfter(LocalDateTime dateTime, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT e FROM EventEntity e WHERE e.id = :id")
    @QueryHints({ @QueryHint(name = "jakarta.persistence.lock.timeout", value = "2000") })
    Optional<EventEntity> findByIdWithLock(@Param("id") Long id);
}