package com.casestudy.BetCaseStudy.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Event {
    private Long id;
    private String league;
    private String homeTeam;
    private String awayTeam;
    private BigDecimal homeWinRate;
    private BigDecimal drawRate;
    private BigDecimal awayWinRate;
    private LocalDateTime startTime;
    private LocalDateTime createdAt;
}