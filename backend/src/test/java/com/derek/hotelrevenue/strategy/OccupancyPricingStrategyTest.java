package com.derek.hotelrevenue.strategy;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class OccupancyPricingStrategyTest {

    private final PricingStrategy pricingStrategy =
            new OccupancyPricingStrategy();

    @Test
    void shouldReturnBasePriceForLowOccupancy() {

        BigDecimal result =
                pricingStrategy.calculatePrice(
                        new BigDecimal("5000"),
                        40.0
                );

        assertEquals(
                new BigDecimal("5000.00"),
                result
        );
    }

    @Test
    void shouldIncreasePriceForHighOccupancy() {

        BigDecimal result =
                pricingStrategy.calculatePrice(
                        new BigDecimal("5000"),
                        88.0
                );

        assertEquals(
                new BigDecimal("6250.00"),
                result
        );
    }

    @Test
    void shouldApplyMaximumAdjustmentForVeryHighOccupancy() {

        BigDecimal result =
                pricingStrategy.calculatePrice(
                        new BigDecimal("5000"),
                        97.0
                );

        assertEquals(
                new BigDecimal("7000.00"),
                result
        );
    }
}