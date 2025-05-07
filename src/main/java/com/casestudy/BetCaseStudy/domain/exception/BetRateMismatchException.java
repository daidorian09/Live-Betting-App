package com.casestudy.BetCaseStudy.domain.exception;

public class BetRateMismatchException extends RuntimeException {
    public BetRateMismatchException(final String message) {
        super(message);
    }
}