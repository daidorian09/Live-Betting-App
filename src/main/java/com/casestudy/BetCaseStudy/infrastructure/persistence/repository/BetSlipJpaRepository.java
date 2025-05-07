package com.casestudy.BetCaseStudy.infrastructure.persistence.repository;

import com.casestudy.BetCaseStudy.infrastructure.persistence.entity.BetSlipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BetSlipJpaRepository extends JpaRepository<BetSlipEntity, Long> {
}
