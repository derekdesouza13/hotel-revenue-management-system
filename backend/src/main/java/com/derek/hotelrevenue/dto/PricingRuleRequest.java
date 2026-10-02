package com.derek.hotelrevenue.dto;

import java.math.BigDecimal;

public class PricingRuleRequest {

    private Long hotelId;
    private String ruleName;
    private BigDecimal occupancyThreshold;
    private BigDecimal adjustmentPercentage;
    private Boolean active;

    public PricingRuleRequest() {
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

    public BigDecimal getOccupancyThreshold() {
        return occupancyThreshold;
    }

    public void setOccupancyThreshold(BigDecimal occupancyThreshold) {
        this.occupancyThreshold = occupancyThreshold;
    }

    public BigDecimal getAdjustmentPercentage() {
        return adjustmentPercentage;
    }

    public void setAdjustmentPercentage(BigDecimal adjustmentPercentage) {
        this.adjustmentPercentage = adjustmentPercentage;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}