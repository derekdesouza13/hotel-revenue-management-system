package com.derek.hotelrevenue.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.derek.hotelrevenue.dto.PricingRecommendationResponse;
import com.derek.hotelrevenue.service.PricingRecommendationService;

@RestController
@RequestMapping("/api/pricing-recommendations")
public class PricingRecommendationController {

    private final PricingRecommendationService
            pricingRecommendationService;

    public PricingRecommendationController(
            PricingRecommendationService pricingRecommendationService
    ) {
        this.pricingRecommendationService =
                pricingRecommendationService;
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<PricingRecommendationResponse>>
    getRecommendationsForHotel(
            @PathVariable Long hotelId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {

        List<PricingRecommendationResponse> recommendations;

        if (date != null) {
            recommendations =
                    pricingRecommendationService
                            .getRecommendationsForHotel(
                                    hotelId,
                                    date
                            );
        } else {
            recommendations =
                    pricingRecommendationService
                            .getRecommendationsForHotel(
                                    hotelId
                            );
        }

        return ResponseEntity.ok(recommendations);
    }
}