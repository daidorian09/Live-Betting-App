package com.casestudy.BetCaseStudy.infrastructure.security;

import com.casestudy.BetCaseStudy.domain.exception.UnauthorizedException;
import com.casestudy.BetCaseStudy.domain.model.Customer;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.casestudy.BetCaseStudy.application.constant.ErrorMessageConstant.UNAUTHORIZED_CUSTOMER_MESSAGE;
import static com.casestudy.BetCaseStudy.application.constant.SecurityConstant.CUSTOMER_ID_HEADER;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MockCustomerProviderTest {

    private MockCustomerProvider mockCustomerProvider;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        mockCustomerProvider = new MockCustomerProvider();
        request = mock(HttpServletRequest.class);
    }

    @Test
    void shouldReturnCustomerWhenHeaderIsValidUUID() {
        final String validId = UUID.randomUUID().toString();
        when(request.getHeader(CUSTOMER_ID_HEADER)).thenReturn(validId);

        final Customer customer = mockCustomerProvider.getCurrentCustomer(request);

        assertThat(customer).isNotNull();
        assertThat(customer.id()).isEqualTo(validId);
        assertThat(customer.username()).isEqualTo("mockUser");
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenHeaderIsInvalidUUID() {
        when(request.getHeader(CUSTOMER_ID_HEADER)).thenReturn("invalid-uuid");

        assertThatThrownBy(() -> mockCustomerProvider.getCurrentCustomer(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(UNAUTHORIZED_CUSTOMER_MESSAGE);
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenHeaderIsMissing() {
        when(request.getHeader(CUSTOMER_ID_HEADER)).thenReturn(null);

        assertThatThrownBy(() -> mockCustomerProvider.getCurrentCustomer(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(UNAUTHORIZED_CUSTOMER_MESSAGE);
    }
}
