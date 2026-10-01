package com.derek.hotelrevenue.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(
        name = "pricing_rules",
        indexes = {
                @Index(name = "idx_pricing_rule_hotel", columnList = "hotel_id")
        }
)
public class PricingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "hotel_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_pricing_rule_hotel")
    )
    private Hotel hotel;

    @Column(name = "rule_name", nullable = false, length = 100)
    private String ruleName;

    @Column(
            name = "occupancy_threshold",
            nullable = false,
            precision = 5,
            scale = 2
    )
    private BigDecimal occupancyThreshold;

    @Column(
            name = "adjustment_percentage",
            nullable = false,
            precision = 6,
            scale = 2
    )
    private BigDecimal adjustmentPercentage;

    @Column(nullable = false)
    private boolean active;

    protected PricingRule() {
        // Required by JPA
    }

    public PricingRule(
            Hotel hotel,
            String ruleName,
            BigDecimal occupancyThreshold,
            BigDecimal adjustmentPercentage
    ) {
        this.hotel = hotel;
        this.ruleName = ruleName;
        this.occupancyThreshold = occupancyThreshold;
        this.adjustmentPercentage = adjustmentPercentage;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public Hotel getHotel() {
        return hotel;
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

    public void setActive(boolean active) {
        this.active = active;
    }
}