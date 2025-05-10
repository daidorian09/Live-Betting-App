package com.casestudy.BetCaseStudy.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object to create a new event")
public record EventRequest(

        @Schema(description = "League name where the match is played",
                example = "Premier League", required = true)
        String league,

        @Schema(description = "Home team name",
                example = "Manchester United", required = true)
        String homeTeam,

        @Schema(description = "Away team name",
                example = "Liverpool", required = true)
        String awayTeam,

        @Schema(description = "Odds for home team to win",
                example = "2.10", required = true)
        BigDecimal homeWinRate,

        @Schema(description = "Odds for a draw",
                example = "3.25", required = true)
        BigDecimal drawRate,

        @Schema(description = "Odds for away team to win",
                example = "2.80", required = true)
        BigDecimal awayWinRate,

        @Schema(description = "Start time of the event in ISO format (UTC preferred)",
                example = "2025-05-10T21:00:00", required = true)
        LocalDateTime startTime
) {}

