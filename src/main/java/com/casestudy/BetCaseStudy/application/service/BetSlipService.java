package com.casestudy.BetCaseStudy.application.service;

import com.casestudy.BetCaseStudy.application.dto.CreateBetSlipRequest;
import com.casestudy.BetCaseStudy.application.usecase.CreateBetSlipUseCase;
import com.casestudy.BetCaseStudy.domain.exception.*;
import com.casestudy.BetCaseStudy.domain.model.BetType;
import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.BetSlipEntity;
import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import com.casestudy.BetCaseStudy.infrastructure.persistence.repository.BetSlipJpaRepository;
import com.casestudy.BetCaseStudy.infrastructure.persistence.repository.EventJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.casestudy.BetCaseStudy.application.constant.ErrorMessageConstant.BETSLIP_TIMEOUT_MESSAGE;
import static com.casestudy.BetCaseStudy.application.constant.ErrorMessageConstant.BET_RATE_CHANGED_MESSAGE;
import static com.casestudy.BetCaseStudy.application.constant.ErrorMessageConstant.EVENT_NOT_FOUND_MESSAGE;
import static com.casestudy.BetCaseStudy.application.constant.ErrorMessageConstant.MULTIPLIER_LIMIT_EXCEEDED_MESSAGE;
import static com.casestudy.BetCaseStudy.application.constant.ErrorMessageConstant.TOTAL_INVESTMENT_LIMIT_EXCEEDED_MESSAGE;

@Service
@RequiredArgsConstructor
public class BetSlipService implements CreateBetSlipUseCase {

    private static final int ZERO = 0;
    @Value("${betcasestudy.betslip.max-multiplier}")
    private int maxMultiplier;
    @Value("${betcasestudy.betslip.timeout-ms}")
    private int betTimeoutMs;
    @Value("${betcasestudy.betslip.max-total-investment}")
    private BigDecimal maxTotalInvestment;

    private final EventJpaRepository eventJpaRepository;
    private final BetSlipJpaRepository betSlipJpaRepository;
    private final DistributedBetSlipLocker distributedBetSlipLocker;

    @Override
    @Retryable(
            value = {LockOperationFailedException.class, RuntimeException.class},
            backoff = @Backoff(delay = 200)
    )
    @Transactional(rollbackFor = {
            BetSlipTimeoutException.class
    })
    public void createBetSlip(final CreateBetSlipRequest request) {
        distributedBetSlipLocker.executeWithLock(request.eventId(), request.selectedBetType(), () -> {

            final long start = System.currentTimeMillis();

            validateLimits(request);

            final EventEntity event = getEvent(request.eventId());

            validateRate(request, event);

            final BetSlipEntity slip = buildBetSlip(request);

            betSlipJpaRepository.saveAndFlush(slip);

            validateTimeout(start);
        });
    }

    private void validateLimits(final CreateBetSlipRequest request) {
        if (request.multiplier() > maxMultiplier) {
            throw new BetSlipLimitExceededException(String.format(MULTIPLIER_LIMIT_EXCEEDED_MESSAGE, maxMultiplier));
        }

        final BigDecimal totalInvestment = request.stake().multiply(BigDecimal.valueOf(request.multiplier()));
        if (totalInvestment.compareTo(maxTotalInvestment) > ZERO) {
            throw new BetSlipLimitExceededException(String.format(TOTAL_INVESTMENT_LIMIT_EXCEEDED_MESSAGE, maxTotalInvestment));
        }
    }

    private EventEntity getEvent(final long eventId) {
        return eventJpaRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(String.format(EVENT_NOT_FOUND_MESSAGE, eventId)));
    }

    private void validateRate(final CreateBetSlipRequest request, final EventEntity event) {
        final BigDecimal currentRate = BetType
                .fromString(request.selectedBetType())
                .getRateFrom(event);

        if (currentRate.compareTo(request.expectedRate()) != ZERO) {
            throw new BetRateMismatchException(String.format(BET_RATE_CHANGED_MESSAGE, currentRate));
        }
    }

    private BetSlipEntity buildBetSlip(final CreateBetSlipRequest request) {
        return BetSlipEntity.builder()
                .eventId(request.eventId())
                .customerId(request.customerId())
                .selectedBetType(request.selectedBetType())
                .stake(request.stake())
                .multiplier(request.multiplier())
                .build();
    }

    private void validateTimeout(final long start) {
        final long elapsed = System.currentTimeMillis() - start;
        if (elapsed > betTimeoutMs) {
            throw new BetSlipTimeoutException(String.format(BETSLIP_TIMEOUT_MESSAGE, betTimeoutMs));
        }
    }
}