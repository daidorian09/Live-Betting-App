package com.casestudy.BetCaseStudy.infrastructure.security;

import com.casestudy.BetCaseStudy.application.constant.SecurityConstant;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class HeaderAuthenticationFilterTest {

    private HeaderAuthenticationFilter filter;
    private FilterChain mockFilterChain;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        filter = new HeaderAuthenticationFilter();
        mockFilterChain = mock(FilterChain.class);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSetAuthenticationWhenValidBasicAuthAndValidCustomerId() throws Exception {
        // Given
        final String credentials = SecurityConstant.BASIC_AUTH_USERNAME + ":" + SecurityConstant.BASIC_AUTH_PASSWORD;
        final String base64Creds = Base64.getEncoder().encodeToString(credentials.getBytes());

        request.addHeader(SecurityConstant.AUTHORIZATION_HEADER, SecurityConstant.AUTHENTICATION_SCHEME + base64Creds);
        request.addHeader(SecurityConstant.CUSTOMER_ID_HEADER, "123e4567-e89b-12d3-a456-426614174000");

        // When
        filter.doFilterInternal(request, response, mockFilterChain);

        // Then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo("123e4567-e89b-12d3-a456-426614174000");

        verify(mockFilterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenMissingAuthorizationHeader() throws Exception {
        request.addHeader(SecurityConstant.CUSTOMER_ID_HEADER, "123e4567-e89b-12d3-a456-426614174000");

        filter.doFilterInternal(request, response, mockFilterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(mockFilterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenInvalidBase64() throws Exception {
        request.addHeader(SecurityConstant.AUTHORIZATION_HEADER, SecurityConstant.AUTHENTICATION_SCHEME + "invalid-base64");
        request.addHeader(SecurityConstant.CUSTOMER_ID_HEADER, "123e4567-e89b-12d3-a456-426614174000");

        filter.doFilterInternal(request, response, mockFilterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(mockFilterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenCustomerIdInvalid() throws Exception {
        final String credentials = SecurityConstant.BASIC_AUTH_USERNAME + ":" + SecurityConstant.BASIC_AUTH_PASSWORD;
        final String base64Creds = Base64.getEncoder().encodeToString(credentials.getBytes());

        request.addHeader(SecurityConstant.AUTHORIZATION_HEADER, SecurityConstant.AUTHENTICATION_SCHEME + base64Creds);
        request.addHeader(SecurityConstant.CUSTOMER_ID_HEADER, "invalid-guid");

        filter.doFilterInternal(request, response, mockFilterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(mockFilterChain).doFilter(request, response);
    }
}
