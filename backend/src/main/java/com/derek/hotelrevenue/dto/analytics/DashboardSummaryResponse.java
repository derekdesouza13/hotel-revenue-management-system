package com.derek.hotelrevenue.dto.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DashboardSummaryResponse {

    private BigDecimal totalRevenue;
    private double occupancyRate;
    private BigDecimal adr;
    private BigDecimal revpar;
    private long totalBookings;

    private LocalDate periodStart;
    private LocalDate periodEnd;

    private List<RevenueTrendPoint> revenueTrend;
    private List<OccupancyTrendPoint> occupancyTrend;
    private List<RecentBookingResponse> recentBookings;

    public DashboardSummaryResponse() {
    }

    public DashboardSummaryResponse(
            BigDecimal totalRevenue,
            double occupancyRate,
            BigDecimal adr,
            BigDecimal revpar,
            long totalBookings,
            LocalDate periodStart,
            LocalDate periodEnd,
            List<RevenueTrendPoint> revenueTrend,
            List<OccupancyTrendPoint> occupancyTrend,
            List<RecentBookingResponse> recentBookings
    ) {
        this.totalRevenue = totalRevenue;
        this.occupancyRate = occupancyRate;
        this.adr = adr;
        this.revpar = revpar;
        this.totalBookings = totalBookings;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.revenueTrend = revenueTrend;
        this.occupancyTrend = occupancyTrend;
        this.recentBookings = recentBookings;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getOccupancyRate() {
        return occupancyRate;
    }

    public void setOccupancyRate(double occupancyRate) {
        this.occupancyRate = occupancyRate;
    }

    public BigDecimal getAdr() {
        return adr;
    }

    public void setAdr(BigDecimal adr) {
        this.adr = adr;
    }

    public BigDecimal getRevpar() {
        return revpar;
    }

    public void setRevpar(BigDecimal revpar) {
        this.revpar = revpar;
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public List<RevenueTrendPoint> getRevenueTrend() {
        return revenueTrend;
    }

    public void setRevenueTrend(
            List<RevenueTrendPoint> revenueTrend
    ) {
        this.revenueTrend = revenueTrend;
    }

    public List<OccupancyTrendPoint> getOccupancyTrend() {
        return occupancyTrend;
    }

    public void setOccupancyTrend(
            List<OccupancyTrendPoint> occupancyTrend
    ) {
        this.occupancyTrend = occupancyTrend;
    }

    public List<RecentBookingResponse> getRecentBookings() {
        return recentBookings;
    }

    public void setRecentBookings(
            List<RecentBookingResponse> recentBookings
    ) {
        this.recentBookings = recentBookings;
    }

    public static class RevenueTrendPoint {

        private String date;
        private BigDecimal revenue;

        public RevenueTrendPoint() {
        }

        public RevenueTrendPoint(
                String date,
                BigDecimal revenue
        ) {
            this.date = date;
            this.revenue = revenue;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public BigDecimal getRevenue() {
            return revenue;
        }

        public void setRevenue(BigDecimal revenue) {
            this.revenue = revenue;
        }
    }

    public static class OccupancyTrendPoint {

        private String date;
        private double occupancyRate;

        public OccupancyTrendPoint() {
        }

        public OccupancyTrendPoint(
                String date,
                double occupancyRate
        ) {
            this.date = date;
            this.occupancyRate = occupancyRate;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public double getOccupancyRate() {
            return occupancyRate;
        }

        public void setOccupancyRate(double occupancyRate) {
            this.occupancyRate = occupancyRate;
        }
    }
}