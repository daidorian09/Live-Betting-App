package com.casestudy.BetCaseStudy.domain.exception;

public class InvalidEventException extends RuntimeException {
    public InvalidEventException(final String message) {
        super(message);
    }
}