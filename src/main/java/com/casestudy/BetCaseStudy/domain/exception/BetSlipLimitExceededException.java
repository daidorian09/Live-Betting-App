package com.casestudy.BetCaseStudy.domain.exception;

public class BetSlipLimitExceededException extends RuntimeException {
    public BetSlipLimitExceededException(final String message) {
        super(message);
    }
}
