package com.casestudy.BetCaseStudy.api.controller;

import com.casestudy.BetCaseStudy.application.dto.CreateBetSlipRequest;
import com.casestudy.BetCaseStudy.application.usecase.CreateBetSlipUseCase;
import com.casestudy.BetCaseStudy.infrastructure.security.MockCustomerProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/betslips")
@RequiredArgsConstructor
public class BetSlipController {

    private final CreateBetSlipUseCase createBetSlipUseCase;
    private final MockCustomerProvider mockCustomerProvider;

    @PostMapping
    public ResponseEntity<Void> createBetSlip(@RequestBody final CreateBetSlipRequest request,
                                              final HttpServletRequest httpRequest) {
        createBetSlipUseCase.createBetSlip(
                new CreateBetSlipRequest(
                        request.eventId(),
                        request.selectedBetType(),
                        mockCustomerProvider.getCurrentCustomer(httpRequest).id(),
                        request.multiplier(),
                        request.stake(),
                        request.expectedRate()
                )
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
