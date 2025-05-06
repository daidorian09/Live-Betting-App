package com.casestudy.BetCaseStudy.api.controller;

import com.casestudy.BetCaseStudy.application.dto.EventRequest;
import com.casestudy.BetCaseStudy.application.usecase.CreateEventUseCase;
import com.casestudy.BetCaseStudy.application.usecase.GetLiveEventsUseCase;
import com.casestudy.BetCaseStudy.domain.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bulletin")
@RequiredArgsConstructor
public class BulletinController {

    private final GetLiveEventsUseCase getLiveEventsUseCase;
    private final CreateEventUseCase createEventUseCase;


    @GetMapping
    public ResponseEntity<List<Event>> getLiveEvents() {
        return ResponseEntity.ok(getLiveEventsUseCase.getLiveEvents());
    }

    @PostMapping("/events")
    public ResponseEntity<?> createEvent(@RequestBody final EventRequest request) {
        createEventUseCase.create(request);
        return ResponseEntity.noContent().build();
    }
}
