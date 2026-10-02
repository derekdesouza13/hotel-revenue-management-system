package com.derek.hotelrevenue.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.derek.hotelrevenue.dto.PricingRuleRequest;
import com.derek.hotelrevenue.dto.PricingRuleResponse;
import com.derek.hotelrevenue.model.Hotel;
import com.derek.hotelrevenue.model.PricingRule;
import com.derek.hotelrevenue.service.HotelService;
import com.derek.hotelrevenue.service.PricingRuleService;

@RestController
@RequestMapping("/api/pricing-rules")
public class PricingRuleController {

    private final PricingRuleService pricingRuleService;
    private final HotelService hotelService;

    public PricingRuleController(
            PricingRuleService pricingRuleService,
            HotelService hotelService
    ) {
        this.pricingRuleService = pricingRuleService;
        this.hotelService = hotelService;
    }

    @GetMapping
    public ResponseEntity<List<PricingRuleResponse>> getAllRules() {

        List<PricingRuleResponse> response =
                pricingRuleService.getAllRules()
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PricingRuleResponse> getRuleById(
            @PathVariable Long id) {

        PricingRule rule = pricingRuleService.getRuleById(id);

        return ResponseEntity.ok(toResponse(rule));
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<PricingRuleResponse>> getRulesByHotel(
            @PathVariable Long hotelId) {

        List<PricingRuleResponse> response =
                pricingRuleService.getRulesByHotel(hotelId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/hotel/{hotelId}/active")
    public ResponseEntity<List<PricingRuleResponse>> getActiveRules(
            @PathVariable Long hotelId) {

        List<PricingRuleResponse> response =
                pricingRuleService.getActiveRules(hotelId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PricingRuleResponse> createRule(
            @RequestBody PricingRuleRequest request) {

        Hotel hotel = hotelService.getHotelById(request.getHotelId());

        PricingRule rule = new PricingRule(
                hotel,
                request.getRuleName(),
                request.getOccupancyThreshold(),
                request.getAdjustmentPercentage()
        );

        if (request.getActive() != null) {
            rule.setActive(request.getActive());
        }

        PricingRule savedRule =
                pricingRuleService.createRule(rule);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(savedRule));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PricingRuleResponse> updateRule(
            @PathVariable Long id,
            @RequestBody PricingRuleRequest request) {

        PricingRule updatedRule = new PricingRule(
                null,
                request.getRuleName(),
                request.getOccupancyThreshold(),
                request.getAdjustmentPercentage()
        );

        if (request.getActive() != null) {
            updatedRule.setActive(request.getActive());
        }

        PricingRule rule =
                pricingRuleService.updateRule(id, updatedRule);

        return ResponseEntity.ok(toResponse(rule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(
            @PathVariable Long id) {

        pricingRuleService.deleteRule(id);

        return ResponseEntity.noContent().build();
    }

    private PricingRuleResponse toResponse(PricingRule rule) {

        return new PricingRuleResponse(
                rule.getId(),
                rule.getHotel().getId(),
                rule.getRuleName(),
                rule.getOccupancyThreshold(),
                rule.getAdjustmentPercentage(),
                rule.isActive()
        );
    }
}