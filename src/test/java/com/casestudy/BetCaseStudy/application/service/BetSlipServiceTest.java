package com.casestudy.BetCaseStudy.application.service;

import com.casestudy.BetCaseStudy.application.dto.CreateBetSlipRequest;
import com.casestudy.BetCaseStudy.domain.exception.BetRateMismatchException;
import com.casestudy.BetCaseStudy.domain.exception.BetSlipLimitExceededException;
import com.casestudy.BetCaseStudy.domain.exception.BetSlipTimeoutException;
import com.casestudy.BetCaseStudy.domain.exception.EventNotFoundException;
import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.BetSlipEntity;
import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import com.casestudy.BetCaseStudy.infrastructure.persistence.repository.BetSlipJpaRepository;
import com.casestudy.BetCaseStudy.infrastructure.persistence.repository.EventJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BetSlipServiceTest {

    @InjectMocks
    private BetSlipService betSlipService;

    @Mock
    private EventJpaRepository eventJpaRepository;

    @Mock
    private BetSlipJpaRepository betSlipJpaRepository;

    @Mock
    private DistributedBetSlipLocker betSlipLocker;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(betSlipService, "maxMultiplier", 10);
        ReflectionTestUtils.setField(betSlipService, "maxTotalInvestment", new BigDecimal("1000"));
        ReflectionTestUtils.setField(betSlipService, "betTimeoutMs", 2000);
    }

    @Test
    void shouldCreateBetSlipWhenValid() {
        // Given
        final CreateBetSlipRequest request = new CreateBetSlipRequest(1L, "HOME_WIN", "cust-1", 2, new BigDecimal("100"), new BigDecimal("1.90"));

        final EventEntity event = new EventEntity();
        event.setId(1L);
        event.setHomeWinRate(new BigDecimal("1.90"));

        when(eventJpaRepository.findById(1L)).thenReturn(Optional.of(event));

        // Action logic is run inside locker
        doAnswer(invocation -> {
            Runnable action = invocation.getArgument(2);
            action.run();
            return null;
        }).when(betSlipLocker).executeWithLock(eq(1L), eq("HOME_WIN"), any());

        // When
        betSlipService.createBetSlip(request);

        // Then
        verify(betSlipJpaRepository).saveAndFlush(any(BetSlipEntity.class));
    }

    @Test
    void shouldThrowWhenMultiplierExceedsLimit() {
        final CreateBetSlipRequest request = new CreateBetSlipRequest(
                1L, "HOME_WIN", "cust-1", 20, new BigDecimal("100"), new BigDecimal("1.90"));

        doAnswer(invocation -> {
            Runnable action = invocation.getArgument(2);
            action.run();
            return null;
        }).when(betSlipLocker).executeWithLock(eq(1L), eq("HOME_WIN"), any());

        assertThatThrownBy(() -> betSlipService.createBetSlip(request))
                .isInstanceOf(BetSlipLimitExceededException.class);
    }


    @Test
    void shouldThrowWhenTotalInvestmentExceedsLimit() {
        final CreateBetSlipRequest request = new CreateBetSlipRequest(
                1L, "HOME_WIN", "cust-1", 2, new BigDecimal("600"), new BigDecimal("1.90"));

        doAnswer(invocation -> {
            Runnable action = invocation.getArgument(2);
            action.run();
            return null;
        }).when(betSlipLocker).executeWithLock(eq(1L), eq("HOME_WIN"), any());


        assertThatThrownBy(() -> betSlipService.createBetSlip(request))
                .isInstanceOf(BetSlipLimitExceededException.class);
    }



    @Test
    void shouldThrowWhenEventNotFound() {
        final CreateBetSlipRequest request = new CreateBetSlipRequest(99L, "HOME_WIN", "cust-1", 2, new BigDecimal("100"), new BigDecimal("1.90"));

        when(eventJpaRepository.findById(99L)).thenReturn(Optional.empty());

        doAnswer(invocation -> {
            Runnable action = invocation.getArgument(2);
            action.run();
            return null;
        }).when(betSlipLocker).executeWithLock(eq(99L), eq("HOME_WIN"), any());

        assertThatThrownBy(() -> betSlipService.createBetSlip(request))
                .isInstanceOf(EventNotFoundException.class);
    }

    @Test
    void shouldThrowWhenRateDoesNotMatch() {
        CreateBetSlipRequest request = new CreateBetSlipRequest(1L, "HOME_WIN", "cust-1", 2, new BigDecimal("100"), new BigDecimal("2.00"));

        final EventEntity event = new EventEntity();
        event.setId(1L);
        event.setHomeWinRate(new BigDecimal("1.90"));

        when(eventJpaRepository.findById(1L)).thenReturn(Optional.of(event));

        doAnswer(invocation -> {
            Runnable action = invocation.getArgument(2);
            action.run();
            return null;
        }).when(betSlipLocker).executeWithLock(eq(1L), eq("HOME_WIN"), any());

        assertThatThrownBy(() -> betSlipService.createBetSlip(request))
                .isInstanceOf(BetRateMismatchException.class);
    }

    @Test
    void shouldThrowWhenTimeoutExceeded() {
        final CreateBetSlipRequest request = new CreateBetSlipRequest(
                1L, "HOME_WIN", "cust-1", 2, new BigDecimal("100"), new BigDecimal("1.90"));

        final EventEntity event = new EventEntity();
        event.setId(1L);
        event.setHomeWinRate(new BigDecimal("1.90"));

        when(eventJpaRepository.findById(1L)).thenAnswer(invocation -> {
            Thread.sleep(3000);
            return Optional.of(event);
        });

        doAnswer(invocation -> {
            Runnable action = invocation.getArgument(2);
            action.run();
            return null;
        }).when(betSlipLocker).executeWithLock(eq(1L), eq("HOME_WIN"), any());

        assertThatThrownBy(() -> betSlipService.createBetSlip(request))
                .isInstanceOf(BetSlipTimeoutException.class);
    }
}