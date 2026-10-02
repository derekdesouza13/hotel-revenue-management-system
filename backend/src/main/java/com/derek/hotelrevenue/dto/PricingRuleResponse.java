package com.derek.hotelrevenue.dto;

import java.math.BigDecimal;

public class PricingRuleResponse {

    private Long id;
    private Long hotelId;
    private String ruleName;
    private BigDecimal occupancyThreshold;
    private BigDecimal adjustmentPercentage;
    private boolean active;

    public PricingRuleResponse() {
    }

    public PricingRuleResponse(
            Long id,
            Long hotelId,
            String ruleName,
            BigDecimal occupancyThreshold,
            BigDecimal adjustmentPercentage,
            boolean active
    ) {
        this.id = id;
        this.hotelId = hotelId;
        this.ruleName = ruleName;
        this.occupancyThreshold = occupancyThreshold;
        this.adjustmentPercentage = adjustmentPercentage;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public BigDecimal getOccupancyThreshold() {
        return occupancyThreshold;
    }

    public BigDecimal getAdjustmentPercentage() {
        return adjustmentPercentage;
    }

    public boolean isActive() {
        return active;
    }
}