package com.casestudy.BetCaseStudy.domain.exception;

public class BetSlipTimeoutException extends RuntimeException {
    public BetSlipTimeoutException(final String message) {
        super(message);
    }
}
