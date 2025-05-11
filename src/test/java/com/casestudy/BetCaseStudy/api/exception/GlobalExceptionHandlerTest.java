package com.casestudy.BetCaseStudy.api.exception;

import com.casestudy.BetCaseStudy.domain.exception.BetRateMismatchException;
import com.casestudy.BetCaseStudy.domain.exception.BetSlipLimitExceededException;
import com.casestudy.BetCaseStudy.domain.exception.BetSlipTimeoutException;
import com.casestudy.BetCaseStudy.domain.exception.EventAlreadyExistsException;
import com.casestudy.BetCaseStudy.domain.exception.EventNotFoundException;
import com.casestudy.BetCaseStudy.domain.exception.InvalidBetTypeException;
import com.casestudy.BetCaseStudy.domain.exception.InvalidEventException;
import com.casestudy.BetCaseStudy.domain.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleInvalidEventException() {
        var response = handler.handleInvalidEventException(new InvalidEventException("Invalid event"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).containsEntry("error", "Invalid Event");
        assertThat(response.getBody()).containsEntry("message", "Invalid event");
    }

    @Test
    void shouldHandleEventAlreadyExistsException() {
        var response = handler.handleEventAlreadyExistsException(new EventAlreadyExistsException("Exists"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).containsEntry("error", "Event Already Exists");
        assertThat(response.getBody()).containsEntry("message", "Exists");
    }

    @Test
    void shouldHandleBetRateMismatchException() {
        var response = handler.handleBetRateMismatchExceptionException(new BetRateMismatchException("Rate mismatch"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("error", "Bet Rate Mismatch");
        assertThat(response.getBody()).containsEntry("message", "Rate mismatch");
    }

    @Test
    void shouldHandleInvalidBetTypeException() {
        var response = handler.handleInvalidBetTypeException(new InvalidBetTypeException("Invalid type"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("error", "Invalid Bet Type");
        assertThat(response.getBody()).containsEntry("message", "Invalid type");
    }

    @Test
    void shouldHandleEventNotFoundException() {
        var response = handler.handleEventNotFoundException(new EventNotFoundException("Not found"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("error", "Event Not Found");
        assertThat(response.getBody()).containsEntry("message", "Not found");
    }

    @Test
    void shouldHandleBetSlipLimitExceededException() {
        var response = handler.handleBetSlipLimitExceededException(new BetSlipLimitExceededException("Limit exceeded"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("error", "Bet Slip Limit Exceeded");
        assertThat(response.getBody()).containsEntry("message", "Limit exceeded");
    }

    @Test
    void shouldHandleBetSlipTimeoutException() {
        var response = handler.handleBetSlipTimeoutException(new BetSlipTimeoutException("Timeout"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("error", "Bet Slip Timeout");
        assertThat(response.getBody()).containsEntry("message", "Timeout");
    }

    @Test
    void shouldHandleUnauthorizedException() {
        var response = handler.handleUnauthorizedException(new UnauthorizedException("Not authorized"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).containsEntry("error", "Unauthorized");
        assertThat(response.getBody()).containsEntry("message", "Not authorized");
    }

    @Test
    void shouldHandleGenericException() {
        var response = handler.handleGenericException(new RuntimeException("Unexpected error"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).containsEntry("error", "Internal Server Error");
        assertThat(response.getBody()).containsEntry("message", "Unexpected error");
    }
}
