package com.casestudy.BetCaseStudy.infrastructure.persistence.repository;

import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.BetSlipEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BetSlipJpaRepositoryTest {

    @Autowired
    private BetSlipJpaRepository betSlipJpaRepository;

    @Test
    void shouldSaveAndFindBetSlipSuccessfully() {
        // given

        final String customerId = UUID.randomUUID().toString();

        final BetSlipEntity betSlip = BetSlipEntity.builder()
                .eventId(1L)
                .customerId(customerId)
                .selectedBetType("HOME_WIN")
                .stake(new BigDecimal("100"))
                .multiplier(2)
                .build();

        // when
        final BetSlipEntity saved = betSlipJpaRepository.save(betSlip);
        final Optional<BetSlipEntity> found = betSlipJpaRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getCustomerId()).isEqualTo(customerId);
        assertThat(found.get().getStake()).isEqualByComparingTo(betSlip.getStake());
    }
}