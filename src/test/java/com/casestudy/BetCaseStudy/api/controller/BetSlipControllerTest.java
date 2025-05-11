package com.casestudy.BetCaseStudy.api.controller;

import com.casestudy.BetCaseStudy.application.dto.CreateBetSlipRequest;
import com.casestudy.BetCaseStudy.application.usecase.CreateBetSlipUseCase;
import com.casestudy.BetCaseStudy.domain.model.Customer;
import com.casestudy.BetCaseStudy.infrastructure.security.MockCustomerProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class BetSlipControllerTest {

    @InjectMocks
    private BetSlipController betSlipController;

    @Mock
    private CreateBetSlipUseCase createBetSlipUseCase;

    @Mock
    private MockCustomerProvider mockCustomerProvider;

    @Mock
    private HttpServletRequest httpRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateBetSlipAndReturnCreatedStatus() {
        // given
        final CreateBetSlipRequest incomingRequest = new CreateBetSlipRequest(
                1L,
                "HOME_WIN",
                null,
                2,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(2.50)
        );

        final String customerId = UUID.randomUUID().toString();
        final String username = "mockUser";

        final Customer mockCustomer = new Customer(customerId, username);
        when(mockCustomerProvider.getCurrentCustomer(httpRequest)).thenReturn(mockCustomer);

        // when
        final ResponseEntity<Void> response = betSlipController.createBetSlip(incomingRequest, httpRequest);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // capture actual request passed to use case
        ArgumentCaptor<CreateBetSlipRequest> captor = ArgumentCaptor.forClass(CreateBetSlipRequest.class);
        verify(createBetSlipUseCase).createBetSlip(captor.capture());

        CreateBetSlipRequest actual = captor.getValue();
        assertThat(actual.eventId()).isEqualTo(incomingRequest.eventId());
        assertThat(actual.selectedBetType()).isEqualTo(incomingRequest.selectedBetType());
        assertThat(actual.customerId()).isEqualTo(customerId);
        assertThat(actual.multiplier()).isEqualTo(incomingRequest.multiplier());
        assertThat(actual.stake()).isEqualByComparingTo(incomingRequest.stake());
        assertThat(actual.expectedRate()).isEqualByComparingTo(incomingRequest.expectedRate());

        verifyNoMoreInteractions(createBetSlipUseCase);
    }
}
