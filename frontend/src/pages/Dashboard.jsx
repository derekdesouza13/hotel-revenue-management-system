import { useEffect, useState } from "react";
import {
  IndianRupee,
  BedDouble,
  TrendingUp,
  CalendarCheck,
} from "lucide-react";
import { motion } from "framer-motion";

import StatCard from "../components/dashboard/StatCard";
import RevenueChart from "../components/dashboard/RevenueChart";
import OccupancyChart from "../components/dashboard/OccupancyChart";
import RecentBookings from "../components/dashboard/RecentBookings";
import PricingAlerts from "../components/dashboard/PricingAlerts";

import apiClient from "../api/client";

function Dashboard() {
  const [analytics, setAnalytics] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchDashboardAnalytics = async () => {
      try {
        setLoading(true);
        setError("");

        const response =
          await apiClient.get("/analytics/dashboard");

        setAnalytics(response.data);
      } catch (err) {
        console.error(
          "Failed to load dashboard analytics:",
          err
        );

        setError(
          "Unable to load dashboard analytics."
        );
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardAnalytics();
  }, []);

  const formatCurrency = (value) => {
    return `₹${Number(value || 0).toLocaleString("en-IN")}`;
  };

  const formatRevenue = (value) => {
    const amount = Number(value || 0);

    if (amount >= 100000) {
      return `₹${(amount / 100000).toFixed(2)}L`;
    }

    if (amount >= 1000) {
      return `₹${(amount / 1000).toFixed(1)}K`;
    }

    return formatCurrency(amount);
  };

  const formatReportingPeriod = () => {
    if (!analytics?.periodStart) {
      return "Current month";
    }

    const date = new Date(
      `${analytics.periodStart}T00:00:00`
    );

    return date.toLocaleDateString("en-IN", {
      month: "long",
      year: "numeric",
    });
  };

  if (loading) {
    return (
      <div className="dashboard">
        <div className="dashboard-loading">
          Loading dashboard...
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="dashboard">
        <div className="dashboard-error">
          <h3>Dashboard unavailable</h3>
          <p>{error}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="dashboard">

      <motion.div
        className="dashboard-intro"
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4 }}
      >
        <div>
          <h1>Revenue Management Dashboard</h1>

          <p>
            Monitor hotel performance, occupancy,
            revenue, and pricing activity.
          </p>
        </div>

        <div className="dashboard-date">
          <span>Performance period</span>

          <strong>
            {formatReportingPeriod()}
          </strong>
        </div>
      </motion.div>

      <div className="stats-grid">

        <StatCard
          title="Total Revenue"
          value={formatRevenue(
            analytics.totalRevenue
          )}
          subtitle="This month"
          icon={IndianRupee}
        />

        <StatCard
          title="Occupancy"
          value={`${analytics.occupancyRate}%`}
          subtitle="This month"
          icon={BedDouble}
        />

        <StatCard
          title="ADR"
          value={formatCurrency(
            analytics.adr
          )}
          subtitle="Average daily rate"
          icon={TrendingUp}
        />

        <StatCard
          title="Bookings"
          value={analytics.totalBookings}
          subtitle="This month"
          icon={CalendarCheck}
        />

      </div>

      <div className="charts-grid">

        <RevenueChart
          data={analytics.revenueTrend}
        />

        <OccupancyChart
          data={analytics.occupancyTrend}
        />

      </div>

      <div className="bottom-grid">

        <RecentBookings />

        <PricingAlerts />

      </div>

    </div>
  );
}

export default Dashboard;