package com.casestudy.BetCaseStudy.infrastructure.scheduler;

import com.casestudy.BetCaseStudy.application.service.EventOddsUpdaterService;
import com.casestudy.BetCaseStudy.application.usecase.EventLockManager;
import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.EventEntity;
import com.casestudy.BetCaseStudy.infrastructure.persistence.repository.EventJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventOddsSchedulerTest {

    @InjectMocks
    private EventOddsScheduler scheduler;

    @Mock
    private EventJpaRepository eventJpaRepository;

    @Mock
    private EventLockManager eventLockManager;

    @Mock
    private EventOddsUpdaterService eventOddsUpdaterService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(scheduler, "pageSize", 10);
    }

    @Test
    void shouldUpdateOddsWhenEventsExist() {
        // given
        final EventEntity event = new EventEntity();
        final Page<EventEntity> page1 = new PageImpl<>(List.of(event), PageRequest.of(0, 10), 1);

        when(eventJpaRepository.findAllByStartTimeAfter(any(), eq(PageRequest.of(0, 10))))
                .thenReturn(page1);

        when(eventLockManager.executeWithLock(eq(event), any()))
                .thenAnswer(invocation -> {
                    @SuppressWarnings("unchecked")
                    Supplier<EventEntity> supplier = (Supplier<EventEntity>) invocation.getArgument(1);
                    return Optional.ofNullable(supplier.get());
                });

        // when
        scheduler.updateOdds();

        // then
        verify(eventOddsUpdaterService).updateEventOdds(event);
        verify(eventJpaRepository).saveAll(List.of(event));
    }

    @Test
    void shouldNotUpdateWhenNoEventsExist() {
        // given
        when(eventJpaRepository.findAllByStartTimeAfter(any(), any())).thenReturn(Page.empty());

        // when
        scheduler.updateOdds();

        // then
        verifyNoInteractions(eventLockManager, eventOddsUpdaterService);
        verify(eventJpaRepository, never()).saveAll(any());
    }
}

