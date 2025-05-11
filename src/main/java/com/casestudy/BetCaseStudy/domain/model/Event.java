package com.casestudy.BetCaseStudy.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Domain model representing a betting event")
public record Event(
        @Schema(description = "Unique identifier of the event", example = "1")
        long id,

        @Schema(description = "Name of the league", example = "Super League")
        String league,

        @Schema(description = "Home team name", example = "Team A")
        String homeTeam,

        @Schema(description = "Away team name", example = "Team B")
        String awayTeam,

        @Schema(description = "Betting rate for home team win", example = "1.85")
        BigDecimal homeWinRate,

        @Schema(description = "Betting rate for draw", example = "3.20")
        BigDecimal drawRate,

        @Schema(description = "Betting rate for away team win", example = "2.10")
        BigDecimal awayWinRate,

        @Schema(description = "Start time of the match", example = "2025-06-01T20:00:00")
        LocalDateTime startTime,

        @Schema(description = "Creation timestamp", example = "2025-05-01T12:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Last update timestamp", example = "2025-05-05T10:00:00")
        LocalDateTime updatedAt
) {}