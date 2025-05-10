package com.casestudy.BetCaseStudy.api.controller;

import com.casestudy.BetCaseStudy.application.dto.EventRequest;
import com.casestudy.BetCaseStudy.application.usecase.CreateEventUseCase;
import com.casestudy.BetCaseStudy.application.usecase.GetLiveEventsUseCase;
import com.casestudy.BetCaseStudy.domain.model.Event;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SecurityRequirement(name = "X-Customer-Id")
@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping("/api/bulletin")
@RequiredArgsConstructor
public class BulletinController {

    private final GetLiveEventsUseCase getLiveEventsUseCase;
    private final CreateEventUseCase createEventUseCase;

    @Operation(
            summary = "Get all live events",
            description = "Returns the list of currently active or live betting events.",
            security = {@SecurityRequirement(name = "X-Customer-Id"), @SecurityRequirement(name = "basicAuth")}
    )
    @GetMapping
    public ResponseEntity<List<Event>> getLiveEvents() {
        return ResponseEntity.ok(getLiveEventsUseCase.getLiveEvents());
    }

    @Operation(
            summary = "Create a new event",
            description = "Creates a new betting event with initial odds and match details.",
            security = {@SecurityRequirement(name = "X-Customer-Id"), @SecurityRequirement(name = "basicAuth")}
    )
    @PostMapping("/events")
    public ResponseEntity<Void> createEvent(@RequestBody final EventRequest request) {
        createEventUseCase.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}