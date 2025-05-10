package com.casestudy.BetCaseStudy.api.controller;

import com.casestudy.BetCaseStudy.application.dto.CreateBetSlipRequest;
import com.casestudy.BetCaseStudy.application.usecase.CreateBetSlipUseCase;
import com.casestudy.BetCaseStudy.infrastructure.security.MockCustomerProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "X-Customer-Id")
@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping("/api/betslips")
@RequiredArgsConstructor
public class BetSlipController {

    private final CreateBetSlipUseCase createBetSlipUseCase;
    private final MockCustomerProvider mockCustomerProvider;

    @Operation(
            summary = "Create a bet slip",
            description = "Creates a new bet slip for a given event with expected odds",
            security = {@SecurityRequirement(name = "X-Customer-Id"),
                    @SecurityRequirement(name = "basicAuth")}
    )
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
