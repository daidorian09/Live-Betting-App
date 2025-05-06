package com.casestudy.BetCaseStudy.application.service;

import com.casestudy.BetCaseStudy.application.constant.ValidationMessageConstant;
import com.casestudy.BetCaseStudy.application.dto.EventRequest;
import com.casestudy.BetCaseStudy.application.usecase.CreateEventUseCase;
import com.casestudy.BetCaseStudy.domain.exception.EventAlreadyExistsException;
import com.casestudy.BetCaseStudy.domain.exception.InvalidEventException;
import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import com.casestudy.BetCaseStudy.infrastructure.persistence.repository.EventJpaRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EventService implements CreateEventUseCase {

    private final EventJpaRepository eventJpaRepository;

    @Override
    public void create(final EventRequest request) {
        validate(request);

        final EventEntity entity = EventEntity.builder()
                .league(request.league())
                .homeTeam(request.homeTeam())
                .awayTeam(request.awayTeam())
                .homeWinRate(request.homeWinRate())
                .drawRate(request.drawRate())
                .awayWinRate(request.awayWinRate())
                .startTime(request.startTime())
                .build();

        eventJpaRepository.save(entity);
    }

    private void validate(final EventRequest request) {
        if (StringUtils.isBlank(request.league())) {
            throw new InvalidEventException(ValidationMessageConstant.LEAGUE_REQUIRED);
        }
        if (StringUtils.isBlank(request.homeTeam())) {
            throw new InvalidEventException(ValidationMessageConstant.HOME_TEAM_REQUIRED);
        }
        if (StringUtils.isBlank(request.awayTeam())) {
            throw new InvalidEventException(ValidationMessageConstant.AWAY_TEAM_REQUIRED);
        }
        if (Objects.isNull(request.homeWinRate()) ||
                Objects.isNull(request.drawRate()) ||
                Objects.isNull(request.awayWinRate())) {
            throw new InvalidEventException(ValidationMessageConstant.RATES_REQUIRED);
        }
        if (request.homeWinRate().compareTo(BigDecimal.ZERO) <= 0 ||
                request.drawRate().compareTo(BigDecimal.ZERO) <= 0 ||
                request.awayWinRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidEventException(ValidationMessageConstant.RATES_MUST_BE_POSITIVE);
        }
        if (Objects.isNull(request.startTime()) || request.startTime().isBefore(LocalDateTime.now())) {
            throw new InvalidEventException(ValidationMessageConstant.START_TIME_INVALID);
        }

        final boolean exists = eventJpaRepository.existsByLeagueAndHomeTeamAndAwayTeamAndStartTime(
                request.league(),
                request.homeTeam(),
                request.awayTeam(),
                request.startTime()
        );

        if (exists) {
            throw new EventAlreadyExistsException(ValidationMessageConstant.EVENT_ALREADY_EXISTS);
        }
    }
}