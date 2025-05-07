package com.casestudy.BetCaseStudy.domain.exception;

public class InvalidBetTypeException extends RuntimeException {
    public InvalidBetTypeException(final String message) {
        super(message);
    }
}