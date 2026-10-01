package com.derek.hotelrevenue.model;

import java.math.BigDecimal;

public class PricingRule {

    private Long id;

    private Long hotelId;

    private String ruleName;

    private double occupancyThreshold;

    private BigDecimal adjustmentPercentage;

    private boolean active;

    public PricingRule() {
        this.active = true;
    }

    public PricingRule(
            Long id,
            Long hotelId,
            String ruleName,
            double occupancyThreshold,
            BigDecimal adjustmentPercentage
    ) {
        this.id = id;
        this.hotelId = hotelId;
        this.ruleName = ruleName;
        this.occupancyThreshold = occupancyThreshold;
        this.adjustmentPercentage = adjustmentPercentage;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public double getOccupancyThreshold() {
        return occupancyThreshold;
    }

    public void setOccupancyThreshold(double occupancyThreshold) {
        this.occupancyThreshold = occupancyThreshold;
    }

    public BigDecimal getAdjustmentPercentage() {
        return adjustmentPercentage;
    }

    public void setAdjustmentPercentage(BigDecimal adjustmentPercentage) {
        this.adjustmentPercentage = adjustmentPercentage;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}