package com.derek.hotelrevenue.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.derek.hotelrevenue.model.PricingRule;

public interface PricingRuleRepository
        extends JpaRepository<PricingRule, Long> {

    List<PricingRule> findByHotelId(Long hotelId);

    List<PricingRule> findByHotelIdAndActiveTrue(Long hotelId);

    Optional<PricingRule>
    findFirstByHotelIdAndActiveTrueAndOccupancyThresholdLessThanEqualOrderByOccupancyThresholdDesc(
            Long hotelId,
            BigDecimal occupancyRate
    );
}