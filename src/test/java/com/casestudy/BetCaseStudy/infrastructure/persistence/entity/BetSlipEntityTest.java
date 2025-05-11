package com.casestudy.BetCaseStudy.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BetSlipEntityTest {

    @Test
    void shouldSetCreatedAtOnPrePersist() {
        // given
        BetSlipEntity slip = BetSlipEntity.builder()
                .eventId(1L)
                .customerId("customer-123")
                .selectedBetType("HOME_WIN")
                .stake(BigDecimal.valueOf(150))
                .multiplier(2)
                .build();

        // when
        slip.prePersist();

        // then
        assertThat(slip.getCreatedAt()).isNotNull();
        assertThat(slip.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void shouldBuildBetSlipEntityCorrectly() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when
        BetSlipEntity slip = BetSlipEntity.builder()
                .id(10L)
                .eventId(1L)
                .customerId("customer-456")
                .selectedBetType("AWAY_WIN")
                .stake(BigDecimal.valueOf(300))
                .multiplier(3)
                .createdAt(now)
                .build();

        // then
        assertThat(slip.getId()).isEqualTo(10L);
        assertThat(slip.getEventId()).isEqualTo(1L);
        assertThat(slip.getCustomerId()).isEqualTo("customer-456");
        assertThat(slip.getSelectedBetType()).isEqualTo("AWAY_WIN");
        assertThat(slip.getStake()).isEqualByComparingTo("300");
        assertThat(slip.getMultiplier()).isEqualTo(3);
        assertThat(slip.getCreatedAt()).isEqualTo(now);
    }
}