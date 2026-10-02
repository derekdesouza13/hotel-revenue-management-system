package com.derek.hotelrevenue.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.derek.hotelrevenue.exception.ResourceNotFoundException;
import com.derek.hotelrevenue.model.PricingRule;
import com.derek.hotelrevenue.repository.PricingRuleRepository;

@Service
public class PricingRuleService {

    private final PricingRuleRepository pricingRuleRepository;

    public PricingRuleService(PricingRuleRepository pricingRuleRepository) {
        this.pricingRuleRepository = pricingRuleRepository;
    }

    public List<PricingRule> getAllRules() {
        return pricingRuleRepository.findAll();
    }

    public PricingRule getRuleById(Long id) {
        return pricingRuleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pricing rule not found with id: " + id
                        ));
    }

    public List<PricingRule> getRulesByHotel(Long hotelId) {
        return pricingRuleRepository.findByHotelId(hotelId);
    }

    public List<PricingRule> getActiveRules(Long hotelId) {
        return pricingRuleRepository.findByHotelIdAndActiveTrue(hotelId);
    }

    public PricingRule createRule(PricingRule rule) {
        return pricingRuleRepository.save(rule);
    }

    public PricingRule updateRule(Long id, PricingRule updatedRule) {

        PricingRule existingRule = getRuleById(id);

        existingRule.setRuleName(updatedRule.getRuleName());

        existingRule.setOccupancyThreshold(
                updatedRule.getOccupancyThreshold()
        );

        existingRule.setAdjustmentPercentage(
                updatedRule.getAdjustmentPercentage()
        );

        existingRule.setActive(updatedRule.isActive());

        return pricingRuleRepository.save(existingRule);
    }

    public void deleteRule(Long id) {

        PricingRule rule = getRuleById(id);

        pricingRuleRepository.delete(rule);
    }
}