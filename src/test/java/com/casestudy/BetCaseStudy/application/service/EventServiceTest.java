package com.casestudy.BetCaseStudy.application.service;

import com.casestudy.BetCaseStudy.application.constant.ErrorMessageConstant;
import com.casestudy.BetCaseStudy.application.dto.EventRequest;
import com.casestudy.BetCaseStudy.domain.exception.EventAlreadyExistsException;
import com.casestudy.BetCaseStudy.domain.exception.InvalidEventException;
import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import com.casestudy.BetCaseStudy.infrastructure.persistence.repository.EventJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventJpaRepository eventJpaRepository;

    private EventRequest baseRequest;

    @BeforeEach
    void setUp() {
        baseRequest = new EventRequest(
                "Premier League",
                "Arsenal",
                "Chelsea",
                BigDecimal.valueOf(1.8),
                BigDecimal.valueOf(3.0),
                BigDecimal.valueOf(4.2),
                LocalDateTime.now().plusHours(1)
        );
    }

    @Test
    void shouldCreateEventWhenRequestIsValid() {
        when(eventJpaRepository.existsByLeagueAndHomeTeamAndAwayTeamAndStartTime(
                any(), any(), any(), any())).thenReturn(false);

        eventService.create(baseRequest);

        verify(eventJpaRepository).save(any(EventEntity.class));
    }

    @ParameterizedTest
    @MethodSource("invalidEventRequests")
    void shouldThrowInvalidEventException(EventRequest request, String expectedMessage) {
        assertThatThrownBy(() -> eventService.create(request))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage(expectedMessage);
    }

    private static Stream<Arguments> invalidEventRequests() {
        LocalDateTime future = LocalDateTime.now().plusHours(1);
        return Stream.of(
                Arguments.of(new EventRequest(null, "A", "B", BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, future),
                        ErrorMessageConstant.LEAGUE_REQUIRED),
                Arguments.of(new EventRequest("L", "", "B", BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, future),
                        ErrorMessageConstant.HOME_TEAM_REQUIRED),
                Arguments.of(new EventRequest("L", "A", "", BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, future),
                        ErrorMessageConstant.AWAY_TEAM_REQUIRED),
                Arguments.of(new EventRequest("L", "A", "B", null, BigDecimal.ONE, BigDecimal.ONE, future),
                        ErrorMessageConstant.RATES_REQUIRED),
                Arguments.of(new EventRequest("L", "A", "B", BigDecimal.ONE, null, BigDecimal.ONE, future),
                        ErrorMessageConstant.RATES_REQUIRED),
                Arguments.of(new EventRequest("L", "A", "B", BigDecimal.ONE, BigDecimal.ONE, null, future),
                        ErrorMessageConstant.RATES_REQUIRED),
                Arguments.of(new EventRequest("L", "A", "B", BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ONE, future),
                        ErrorMessageConstant.RATES_MUST_BE_POSITIVE),
                Arguments.of(new EventRequest("L", "A", "B", BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ONE, future),
                        ErrorMessageConstant.RATES_MUST_BE_POSITIVE),
                Arguments.of(new EventRequest("L", "A", "B", BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ZERO, future),
                        ErrorMessageConstant.RATES_MUST_BE_POSITIVE),
                Arguments.of(new EventRequest("L", "A", "B", BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, null),
                        ErrorMessageConstant.START_TIME_INVALID),
                Arguments.of(new EventRequest("L", "A", "B", BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                                LocalDateTime.now().minusMinutes(10)),
                        ErrorMessageConstant.START_TIME_INVALID)
        );
    }

    @Test
    void shouldThrowWhenEventAlreadyExists() {
        when(eventJpaRepository.existsByLeagueAndHomeTeamAndAwayTeamAndStartTime(
                any(), any(), any(), any())).thenReturn(true);

        assertThatThrownBy(() -> eventService.create(baseRequest))
                .isInstanceOf(EventAlreadyExistsException.class)
                .hasMessage(ErrorMessageConstant.EVENT_ALREADY_EXISTS);
    }
}