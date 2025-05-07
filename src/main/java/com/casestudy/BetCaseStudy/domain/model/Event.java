package com.casestudy.BetCaseStudy.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Event(
        Long id,
        String league,
        String homeTeam,
        String awayTeam,
        BigDecimal homeWinRate,
        BigDecimal drawRate,
        BigDecimal awayWinRate,
        LocalDateTime startTime,
        LocalDateTime createdAt
) {}