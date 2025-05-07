package com.casestudy.BetCaseStudy.domain.model;

import com.casestudy.BetCaseStudy.domain.exception.InvalidBetTypeException;
import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;

import java.math.BigDecimal;

public enum BetType {
    HOME_WIN {
        public BigDecimal getRateFrom(final EventEntity event) { return event.getHomeWinRate(); }
    },
    DRAW {
        public BigDecimal getRateFrom(final EventEntity event) { return event.getDrawRate(); }
    },
    AWAY_WIN {
        public BigDecimal getRateFrom(final EventEntity event) { return event.getAwayWinRate(); }
    };

    public abstract BigDecimal getRateFrom(final EventEntity event);

    public static BetType fromString(final String value) {
        try {
            return BetType.valueOf(value.toUpperCase());
        } catch (final Exception ex) {
            throw new InvalidBetTypeException("Invalid bet type: " + value);
        }
    }
}
