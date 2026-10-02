package com.derek.hotelrevenue.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.derek.hotelrevenue.model.PricingRule;

public interface PricingRuleRepository
        extends JpaRepository<PricingRule, Long> {

    List<PricingRule> findByHotelId(Long hotelId);

    List<PricingRule> findByHotelIdAndActiveTrue(Long hotelId);
}