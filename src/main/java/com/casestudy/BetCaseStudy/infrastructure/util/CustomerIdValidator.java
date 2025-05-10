package com.casestudy.BetCaseStudy.infrastructure.util;

import lombok.experimental.UtilityClass;

import static java.util.UUID.fromString;

@UtilityClass
public class CustomerIdValidator {
    public static boolean isValidCustomerId(final String customerId) {
        try {
            fromString(customerId);
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}