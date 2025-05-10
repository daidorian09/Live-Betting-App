package com.casestudy.BetCaseStudy.infrastructure.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CustomerIdValidatorTest {

    @Test
    void shouldReturnTrueForValidUUID() {
        String validUuid = "123e4567-e89b-12d3-a456-426614174000";
        assertTrue(CustomerIdValidator.isValidCustomerId(validUuid));
    }

    @Test
    void shouldReturnFalseForInvalidUUID() {
        String invalidUuid = "not-a-valid-uuid";
        assertFalse(CustomerIdValidator.isValidCustomerId(invalidUuid));
    }

    @Test
    void shouldReturnFalseForNullValue() {
        assertFalse(CustomerIdValidator.isValidCustomerId(null));
    }

    @Test
    void shouldReturnFalseForEmptyString() {
        assertFalse(CustomerIdValidator.isValidCustomerId(""));
    }
}