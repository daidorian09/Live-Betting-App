package com.casestudy.BetCaseStudy.infrastructure.security;

import com.casestudy.BetCaseStudy.application.constant.SecurityConstant;
import com.casestudy.BetCaseStudy.domain.exception.UnauthorizedException;
import com.casestudy.BetCaseStudy.domain.model.Customer;
import com.casestudy.BetCaseStudy.infrastructure.util.CustomerIdValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.casestudy.BetCaseStudy.application.constant.ErrorMessageConstant.UNAUTHORIZED_CUSTOMER_MESSAGE;

@Component
public class MockCustomerProvider {

    private static final String MOCK_USER_NAME = "mockUser";

    public Customer getCurrentCustomer(final HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(SecurityConstant.CUSTOMER_ID_HEADER))
                .filter(CustomerIdValidator::isValidCustomerId)
                .map(id -> new Customer(id, MOCK_USER_NAME))
                .orElseThrow(() -> new UnauthorizedException(UNAUTHORIZED_CUSTOMER_MESSAGE));
    }
}
