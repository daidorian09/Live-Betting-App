package com.casestudy.BetCaseStudy.application.usecase;

import com.casestudy.BetCaseStudy.application.dto.CreateBetSlipRequest;

public interface CreateBetSlipUseCase {
    void createBetSlip(final CreateBetSlipRequest request);
}
