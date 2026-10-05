package com.derek.hotelrevenue.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.derek.hotelrevenue.dto.analytics.DashboardSummaryResponse;
import com.derek.hotelrevenue.service.analytics.AnalyticsService;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardSummaryResponse> getDashboardSummary() {

        DashboardSummaryResponse response =
                analyticsService.getDashboardSummary();

        return ResponseEntity.ok(response);
    }
}