package com.casestudy.BetCaseStudy.domain.exception;

public class EventAlreadyExistsException extends RuntimeException {
    public EventAlreadyExistsException(final String message) {
        super(message);
    }
}