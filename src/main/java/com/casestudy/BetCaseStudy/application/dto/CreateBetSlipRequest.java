package com.casestudy.BetCaseStudy.application.dto;

import java.math.BigDecimal;

public record CreateBetSlipRequest(
        long eventId,
        String selectedBetType,
        String customerId,
        int multiplier,
        BigDecimal stake,
        BigDecimal expectedRate
) {}
