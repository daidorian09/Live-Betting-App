package com.casestudy.BetCaseStudy.application.service;


import com.casestudy.BetCaseStudy.application.dto.EventRequest;
import com.casestudy.BetCaseStudy.domain.exception.EventAlreadyExistsException;
import com.casestudy.BetCaseStudy.domain.exception.InvalidEventException;
import com.casestudy.BetCaseStudy.infrastructure.persistence.repository.EventJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class EventServiceIntegrationTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventJpaRepository eventJpaRepository;

    private EventRequest validRequest;

    @BeforeEach
    void setUp() {
        eventJpaRepository.deleteAll();

        validRequest = new EventRequest(
                "Süper Lig",
                "Fenerbahçe",
                "Galatasaray",
                BigDecimal.valueOf(2.1),
                BigDecimal.valueOf(3.0),
                BigDecimal.valueOf(3.2),
                LocalDateTime.now().plusDays(1)
        );
    }

    @Test
    void shouldCreateEventSuccessfully() {
        // WHEN
        eventService.create(validRequest);

        // THEN
        var savedEvents = eventJpaRepository.findAll();
        assertThat(savedEvents).hasSize(1);
        assertThat(savedEvents.get(0).getHomeTeam()).isEqualTo("Fenerbahçe");
    }

    @Test
    void shouldThrowIfLeagueIsBlank() {
        EventRequest badRequest = new EventRequest(
                "",
                "Fenerbahçe",
                "Galatasaray",
                BigDecimal.valueOf(2),
                BigDecimal.valueOf(3),
                BigDecimal.valueOf(4),
                LocalDateTime.now().plusHours(1)
        );

        assertThrows(InvalidEventException.class, () -> eventService.create(badRequest));
    }

    @Test
    void shouldThrowIfEventAlreadyExists() {
        // GIVEN
        eventService.create(validRequest);

        // WHEN + THEN
        assertThrows(EventAlreadyExistsException.class, () -> eventService.create(validRequest));
    }

    @Test
    void shouldThrowIfStartTimeIsInThePast() {
        EventRequest request = new EventRequest(
                "Süper Lig",
                "FB",
                "GS",
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE,
                LocalDateTime.now().minusHours(1)
        );

        assertThrows(InvalidEventException.class, () -> eventService.create(request));
    }
}

