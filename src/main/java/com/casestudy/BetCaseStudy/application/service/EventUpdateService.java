package com.casestudy.BetCaseStudy.application.service;

import com.casestudy.BetCaseStudy.application.usecase.UpdateEventOddsUseCase;
import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import com.casestudy.BetCaseStudy.infrastructure.persistence.repository.EventJpaRepository;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EventUpdateService implements UpdateEventOddsUseCase {

    public static final double MIN_FACTOR = 0.95;
    public static final double MAX_FACTOR = 1.05;
    public static final int SCALE = 2;
    private final EventJpaRepository eventJpaRepository;
    private final Random random = new Random();

    @Value("${betcasestudy.broadcast.page-size}")
    private int pageSize;

    @Override
    @Scheduled(fixedRate = 10000) // every 10 seconds
    @SchedulerLock(
            name = "updateEventOdds",
            lockAtMostFor = "PT20S",  // fail-safe: max job duration
            lockAtLeastFor = "PT5S"   // prevents overlapping executions
    )
    public void updateOdds() {
        int page = 0;
        Page<EventEntity> pageResult;

        do {
            pageResult = eventJpaRepository.findAllByStartTimeAfter(LocalDateTime.now(), PageRequest.of(page, pageSize));
            final List<EventEntity> events = pageResult.getContent();

            events.forEach(event -> {
                event.setHomeWinRate(randomize(event.getHomeWinRate()));
                event.setDrawRate(randomize(event.getDrawRate()));
                event.setAwayWinRate(randomize(event.getAwayWinRate()));
            });

            eventJpaRepository.saveAll(events);
            page++;
        } while (!pageResult.isLast());
    }

    private BigDecimal randomize(BigDecimal value) {
        double factor = MIN_FACTOR + (MAX_FACTOR - MIN_FACTOR) * random.nextDouble();
        return value.multiply(BigDecimal.valueOf(factor)).setScale(SCALE, RoundingMode.HALF_UP);
    }
}
