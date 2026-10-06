package com.derek.hotelrevenue.dto;

import java.math.BigDecimal;

public class PricingRecommendationResponse {

    private final Long roomId;
    private final String roomNumber;
    private final BigDecimal currentPrice;
    private final BigDecimal occupancyRate;
    private final BigDecimal adjustmentPercentage;
    private final BigDecimal recommendedPrice;
    private final String recommendation;
    private final String reason;

    public PricingRecommendationResponse(
            Long roomId,
            String roomNumber,
            BigDecimal currentPrice,
            BigDecimal occupancyRate,
            BigDecimal adjustmentPercentage,
            BigDecimal recommendedPrice,
            String recommendation,
            String reason
    ) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.currentPrice = currentPrice;
        this.occupancyRate = occupancyRate;
        this.adjustmentPercentage = adjustmentPercentage;
        this.recommendedPrice = recommendedPrice;
        this.recommendation = recommendation;
        this.reason = reason;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public BigDecimal getOccupancyRate() {
        return occupancyRate;
    }

    public BigDecimal getAdjustmentPercentage() {
        return adjustmentPercentage;
    }

    public BigDecimal getRecommendedPrice() {
        return recommendedPrice;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public String getReason() {
        return reason;
    }
}