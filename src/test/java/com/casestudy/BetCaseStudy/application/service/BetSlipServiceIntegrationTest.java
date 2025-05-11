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
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BetSlipServiceIntegrationTest {

    @Autowired
    private BetSlipService betSlipService;

    @Autowired
    private EventJpaRepository eventJpaRepository;

    @Autowired
    private BetSlipJpaRepository betSlipJpaRepository;

    @MockBean
    private DistributedBetSlipLocker distributedBetSlipLocker;

    private EventEntity event;

    @BeforeEach
    void setUp() {
        betSlipJpaRepository.deleteAll();
        eventJpaRepository.deleteAll();

        event = eventJpaRepository.save(
                EventEntity.builder()
                        .league("Premier League")
                        .homeTeam("Team A")
                        .awayTeam("Team B")
                        .homeWinRate(BigDecimal.valueOf(2.0))
                        .drawRate(BigDecimal.valueOf(3.0))
                        .awayWinRate(BigDecimal.valueOf(2.5))
                        .startTime(LocalDateTime.now().plusHours(1))
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        doAnswer(invocation -> {
            Runnable action = invocation.getArgument(2);
            action.run();
            return null;
        }).when(distributedBetSlipLocker).executeWithLock(anyLong(), anyString(), any());
    }

    @Test
    void shouldCreateBetSlipSuccessfully() {
        // given
        final CreateBetSlipRequest request = new CreateBetSlipRequest(
                event.getId(),
                "HOME_WIN",
                "customer-123",
                2,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(2.0)
        );

        // when
        betSlipService.createBetSlip(request);

        // then
        final List<BetSlipEntity> slips = betSlipJpaRepository.findAll();
        assertThat(slips.size()).isOne();

        final BetSlipEntity saved = slips.get(0);
        assertThat(saved.getEventId()).isEqualTo(event.getId());
        assertThat(saved.getCustomerId()).isEqualTo("customer-123");
        assertThat(saved.getMultiplier()).isEqualTo(2);
        assertThat(saved.getStake()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(saved.getSelectedBetType()).isEqualTo("HOME_WIN");
    }

    @Test
    void shouldThrowEventNotFoundExceptionWhenEventDoesNotExist() {
        // given
        final CreateBetSlipRequest request = new CreateBetSlipRequest(
                -1L, "HOME_WIN", "customer-123", 1, BigDecimal.TEN, BigDecimal.valueOf(2.0)
        );

        // when - then
        assertThrows(EventNotFoundException.class, () -> betSlipService.createBetSlip(request));
    }

    @Test
    void shouldThrowBetRateMismatchExceptionWhenRatesAreDifferent() {
        // given
        final CreateBetSlipRequest request = new CreateBetSlipRequest(
                event.getId(), "HOME_WIN", "customer-123", 1, BigDecimal.TEN, BigDecimal.valueOf(99.9)
        );

        // when - then
        assertThrows(BetRateMismatchException.class, () -> betSlipService.createBetSlip(request));
    }

    @Test
    void shouldThrowLimitExceededExceptionWhenMultiplierTooHigh() {
        // given
        final CreateBetSlipRequest request = new CreateBetSlipRequest(
                event.getId(), "HOME_WIN", "customer-123", 999, BigDecimal.ONE, BigDecimal.valueOf(2.0)
        );

        // when - then
        assertThrows(BetSlipLimitExceededException.class, () -> betSlipService.createBetSlip(request));
    }

    @Test
    void shouldThrowLimitExceededExceptionWhenTotalInvestmentTooHigh() {
        // given
        final CreateBetSlipRequest request = new CreateBetSlipRequest(
                event.getId(), "HOME_WIN", "customer-123", 1000, BigDecimal.valueOf(300), BigDecimal.valueOf(2.0)
        );

        // when - then
        assertThrows(BetSlipLimitExceededException.class, () -> betSlipService.createBetSlip(request));
    }

    @Test
    void shouldRetryThreeTimesWhenTimeoutOccurs() {
        // given
        ReflectionTestUtils.setField(betSlipService, "betTimeoutMs", 2000);
        final CreateBetSlipRequest request = new CreateBetSlipRequest(
                event.getId(), "HOME_WIN", "customer-123", 1, BigDecimal.TEN, BigDecimal.valueOf(2.0)
        );

        // simulate timeout on every retry
        doAnswer(invocation -> {
            Runnable action = invocation.getArgument(2);
            action.run();
            throw new BetSlipTimeoutException("Simulated timeout");
        }).when(distributedBetSlipLocker).executeWithLock(anyLong(), anyString(), any());

        // then
        assertThrows(BetSlipTimeoutException.class, () -> betSlipService.createBetSlip(request));
        verify(distributedBetSlipLocker, times(3)).executeWithLock(anyLong(), anyString(), any());
    }
}