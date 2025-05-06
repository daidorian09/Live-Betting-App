package com.casestudy.BetCaseStudy.api.exception;

import com.casestudy.BetCaseStudy.domain.exception.EventAlreadyExistsException;
import com.casestudy.BetCaseStudy.domain.exception.InvalidEventException;
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
        log.warn("InvalidEventException occurred: {}", ex.getMessage());

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
        log.warn("EventAlreadyExistsException occurred: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Event Already Exists",
                        "message", ex.getMessage(),
                        "status", HttpStatus.CONFLICT.value()
                )
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleGenericException(final Exception ex) {
        log.error("Unexpected exception occurred", ex);

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
