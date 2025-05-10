package com.casestudy.BetCaseStudy.domain.exception;

public class LockOperationFailedException extends RuntimeException {
    public LockOperationFailedException(final String message) {
        super(message);
    }
}
