package com.casestudy.BetCaseStudy.infrastructure.security;

import com.casestudy.BetCaseStudy.domain.exception.UnauthorizedException;
import com.casestudy.BetCaseStudy.domain.model.Customer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.casestudy.BetCaseStudy.application.constant.ErrorMessageConstant.UNAUTHORIZED_CUSTOMER_MESSAGE;
import static java.util.UUID.fromString;

@Component
public class MockCustomerProvider {

    public Customer getCurrentCustomer(final HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("X-Customer-Id"))
                .filter(this::isValidCustomerId)
                .map(id -> new Customer(id, "mockUser"))
                .orElseThrow(() -> new UnauthorizedException(UNAUTHORIZED_CUSTOMER_MESSAGE));
    }

    private boolean isValidCustomerId(final String customerId) {
        try {
            fromString(customerId);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
