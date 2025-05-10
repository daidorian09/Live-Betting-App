package com.casestudy.BetCaseStudy.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Request object to create a new bet slip")
public record CreateBetSlipRequest(

        @Schema(description = "ID of the event on which the bet is placed",
                example = "101", required = true)
        long eventId,

        @Schema(description = "Type of bet selected (e.g., HOME_WIN, DRAW, AWAY_WIN)",
                example = "HOME_WIN", required = true)
        String selectedBetType,

        @Schema(description = "Unique ID of the customer placing the bet",
                example = "c3d1f1a3-2e3a-43b0-9239-ecf79fa17b27", required = true, hidden = true)
        String customerId,

        @Schema(description = "Multiplier applied to this bet",
                example = "2", minimum = "1", maximum = "10", required = true)
        int multiplier,

        @Schema(description = "Stake amount in Turkish Lira",
                example = "150.00", required = true)
        BigDecimal stake,

        @Schema(description = "Expected odds at the time the bet is placed",
                example = "1.85", required = true)
        BigDecimal expectedRate
) {}