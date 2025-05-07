package com.casestudy.BetCaseStudy.api.exception;

import com.casestudy.BetCaseStudy.domain.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidEventException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleInvalidEventException(final InvalidEventException ex) {
        log.warn("InvalidEventException occurred: ", ex);

        return ResponseEntity.unprocessableEntity().body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Invalid Event",
                        "message", ex.getMessage(),
                        "status", HttpStatus.UNPROCESSABLE_ENTITY.value()
                )
        );
    }

    @ExceptionHandler(EventAlreadyExistsException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleEventAlreadyExistsException(final EventAlreadyExistsException ex) {
        log.warn("EventAlreadyExistsException occurred: ", ex);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Event Already Exists",
                        "message", ex.getMessage(),
                        "status", HttpStatus.CONFLICT.value()
                )
        );
    }

    @ExceptionHandler(BetRateMismatchException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleBetRateMismatchExceptionException(final BetRateMismatchException ex) {
        log.warn("BetRateMismatchException occurred: ", ex);

        return ResponseEntity.badRequest().body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Bet Rate Mismatch",
                        "message", ex.getMessage(),
                        "status", HttpStatus.BAD_REQUEST.value()
                )
        );
    }

    @ExceptionHandler(InvalidBetTypeException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleInvalidBetTypeException(final InvalidBetTypeException ex) {
        log.warn("InvalidBetTypeException occurred: ", ex);

        return ResponseEntity.badRequest().body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Invalid Bet Type",
                        "message", ex.getMessage(),
                        "status", HttpStatus.BAD_REQUEST.value()
                )
        );
    }

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleEventNotFoundException(final EventNotFoundException ex) {
        log.warn("EventNotFoundException occurred: ", ex);

        return ResponseEntity.badRequest().body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Event Not Found",
                        "message", ex.getMessage(),
                        "status", HttpStatus.BAD_REQUEST.value()
                )
        );
    }

    @ExceptionHandler(BetSlipLimitExceededException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleBetSlipLimitExceededException(final BetSlipLimitExceededException ex) {
        log.warn("BetSlipLimitExceededException occurred: ", ex);

        return ResponseEntity.badRequest().body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Bet Slip Limit Exceeded",
                        "message", ex.getMessage(),
                        "status", HttpStatus.BAD_REQUEST.value()
                )
        );
    }

    @ExceptionHandler(BetSlipTimeoutException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleBetSlipTimeoutException(final BetSlipTimeoutException ex) {
        log.warn("BetSlipTimeoutException occurred: ", ex);

        return ResponseEntity.badRequest().body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Bet Slip Timeout",
                        "message", ex.getMessage(),
                        "status", HttpStatus.BAD_REQUEST.value()
                )
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(final UnauthorizedException ex) {
        log.warn("UnauthorizedException occurred: ", ex);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Unauthorized",
                        "message", ex.getMessage(),
                        "status", HttpStatus.UNAUTHORIZED.value()
                )
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleGenericException(final Exception ex) {
        log.error("Unexpected exception occurred : ", ex);

        return ResponseEntity.internalServerError().body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Internal Server Error",
                        "message", ex.getMessage(),
                        "status", HttpStatus.INTERNAL_SERVER_ERROR.value()
                )
        );
    }
}