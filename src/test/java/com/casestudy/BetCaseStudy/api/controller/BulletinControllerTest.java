package com.casestudy.BetCaseStudy.api.controller;

import com.casestudy.BetCaseStudy.application.dto.EventRequest;
import com.casestudy.BetCaseStudy.application.usecase.CreateEventUseCase;
import com.casestudy.BetCaseStudy.application.usecase.GetLiveEventsUseCase;
import com.casestudy.BetCaseStudy.domain.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BulletinControllerTest {

    @InjectMocks
    private BulletinController bulletinController;

    @Mock
    private GetLiveEventsUseCase getLiveEventsUseCase;

    @Mock
    private CreateEventUseCase createEventUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnListOfLiveEvents() {
        // given
        final List<Event> mockEvents = Collections.singletonList(
                new Event(1L, "Super League", "Team A", "Team B",
                        BigDecimal.valueOf(2.1), BigDecimal.valueOf(3.4), BigDecimal.valueOf(1.9),
                        LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(4)));
        when(getLiveEventsUseCase.getLiveEvents()).thenReturn(mockEvents);

        // when
        final ResponseEntity<List<Event>> response = bulletinController.getLiveEvents();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockEvents);
        verify(getLiveEventsUseCase, times(1)).getLiveEvents();
    }

    @Test
    void shouldCreateEventSuccessfully() {
        // given
        final EventRequest request = new EventRequest(
                "Premier League",
                "Team X",
                "Team Y",
                BigDecimal.valueOf(1.5),
                BigDecimal.valueOf(3.0),
                BigDecimal.valueOf(2.8),
                LocalDateTime.now().plusDays(1)
        );

        // when
        final ResponseEntity<Void> response = bulletinController.createEvent(request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(createEventUseCase, times(1)).create(request);
    }
}
