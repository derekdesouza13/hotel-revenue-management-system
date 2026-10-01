package com.derek.hotelrevenue.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class OccupancyPricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculatePrice(
            BigDecimal basePrice,
            double occupancy
    ) {

        BigDecimal adjustmentPercentage;

        if (occupancy < 50.0) {
            adjustmentPercentage = BigDecimal.ZERO;
        } else if (occupancy < 70.0) {
            adjustmentPercentage = new BigDecimal("5");
        } else if (occupancy < 85.0) {
            adjustmentPercentage = new BigDecimal("15");
        } else if (occupancy < 95.0) {
            adjustmentPercentage = new BigDecimal("25");
        } else {
            adjustmentPercentage = new BigDecimal("40");
        }

        BigDecimal multiplier = BigDecimal.ONE.add(
                adjustmentPercentage
                        .divide(
                                new BigDecimal("100"),
                                4,
                                RoundingMode.HALF_UP
                        )
        );

        return basePrice
                .multiply(multiplier)
                .setScale(2, RoundingMode.HALF_UP);
    }
}