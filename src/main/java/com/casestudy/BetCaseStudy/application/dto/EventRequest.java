package com.casestudy.BetCaseStudy.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventRequest(
        String league,
        String homeTeam,
        String awayTeam,
        BigDecimal homeWinRate,
        BigDecimal drawRate,
        BigDecimal awayWinRate,
        LocalDateTime startTime
) {}
