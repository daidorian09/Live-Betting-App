package com.casestudy.BetCaseStudy.infrastructure.scheduler;

import com.casestudy.BetCaseStudy.application.service.EventOddsUpdaterService;
import com.casestudy.BetCaseStudy.application.usecase.EventLockManager;
import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import com.casestudy.BetCaseStudy.infrastructure.persistence.repository.EventJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class EventOddsScheduler {

    private static final long FIVE_MINUTES_MS = 5 * 60 * 1000;
    private final EventJpaRepository eventJpaRepository;
    private final EventLockManager eventLockManager;
    private final EventOddsUpdaterService eventOddsUpdaterService;

    @Value("${betcasestudy.broadcast.page-size}")
    private int pageSize;

    @Scheduled(fixedRate = FIVE_MINUTES_MS)
    @SchedulerLock(name = "updateEventOdds", lockAtMostFor = "PT20S", lockAtLeastFor = "PT5S")
    public void updateOdds() {
        int page = 0;
        Page<EventEntity> pageResult;

        do {
            pageResult = eventJpaRepository.findAllByStartTimeAfter(
                    LocalDateTime.now(), PageRequest.of(page, pageSize));

            final List<EventEntity> updatedEvents = new ArrayList<>();

            pageResult
                    .getContent()
                    .forEach(event -> eventLockManager.executeWithLock(event, () -> {
                        eventOddsUpdaterService.updateEventOdds(event);
                        return event;
                    }).ifPresent(updatedEvents::add));


            if (!CollectionUtils.isEmpty(updatedEvents)) {
                eventJpaRepository.saveAll(updatedEvents);
            }

            page++;
        } while (!pageResult.isLast());
    }
}