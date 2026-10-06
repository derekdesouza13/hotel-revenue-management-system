import { useEffect, useState } from "react";
import {
  IndianRupee,
  BedDouble,
  TrendingUp,
  CalendarCheck,
  ArrowUp,
  ArrowDown,
  Minus,
  Check,
  X,
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
  const [pricingRecommendations, setPricingRecommendations] =
    useState([]);

  const [loading, setLoading] = useState(true);
  const [pricingLoading, setPricingLoading] = useState(true);

  const [error, setError] = useState("");
  const [pricingError, setPricingError] = useState("");

  const [updatingRoomId, setUpdatingRoomId] = useState(null);
  const [actionMessage, setActionMessage] = useState("");

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);
      setPricingLoading(true);
      setError("");
      setPricingError("");

      const [
        analyticsResponse,
        pricingResponse,
      ] = await Promise.all([
        apiClient.get("/analytics/dashboard"),
        apiClient.get(
          "/pricing-recommendations/hotel/1"
        ),
      ]);

      setAnalytics(analyticsResponse.data);

      setPricingRecommendations(
        pricingResponse.data
      );
    } catch (err) {
      console.error(
        "Failed to load dashboard data:",
        err
      );

      if (
        err.config?.url?.includes(
          "/pricing-recommendations"
        )
      ) {
        setPricingError(
          "Unable to load pricing recommendations."
        );
      } else {
        setError(
          "Unable to load dashboard analytics."
        );
      }
    } finally {
      setLoading(false);
      setPricingLoading(false);
    }
  };

  const handleAcceptRecommendation = async (
    recommendation
  ) => {
    try {
      setUpdatingRoomId(
        recommendation.roomId
      );

      setActionMessage("");

      await apiClient.put(
        `/rooms/${recommendation.roomId}/price`,
        null,
        {
          params: {
            price:
              recommendation.recommendedPrice,
          },
        }
      );

      setActionMessage(
        `Room ${recommendation.roomNumber} price updated successfully.`
      );

      await refreshPricingRecommendations();

    } catch (err) {
      console.error(
        "Failed to accept pricing recommendation:",
        err
      );

      setActionMessage(
        `Unable to update Room ${recommendation.roomNumber} price.`
      );
    } finally {
      setUpdatingRoomId(null);
    }
  };

  const handleRejectRecommendation = (
    recommendation
  ) => {
    setActionMessage(
      `Recommendation for Room ${recommendation.roomNumber} was rejected.`
    );
  };

  const refreshPricingRecommendations = async () => {
    try {
      setPricingLoading(true);

      const response =
        await apiClient.get(
          "/pricing-recommendations/hotel/1"
        );

      setPricingRecommendations(
        response.data
      );
    } catch (err) {
      console.error(
        "Failed to refresh pricing recommendations:",
        err
      );

      setPricingError(
        "Unable to refresh pricing recommendations."
      );
    } finally {
      setPricingLoading(false);
    }
  };

  const formatCurrency = (value) => {
    return `₹${Number(value || 0).toLocaleString(
      "en-IN"
    )}`;
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

  const getRecommendationIcon = (
    recommendation
  ) => {
    if (recommendation === "INCREASE") {
      return <ArrowUp size={18} />;
    }

    if (recommendation === "DECREASE") {
      return <ArrowDown size={18} />;
    }

    return <Minus size={18} />;
  };

  const getRecommendationClass = (
    recommendation
  ) => {
    if (recommendation === "INCREASE") {
      return "pricing-recommendation increase";
    }

    if (recommendation === "DECREASE") {
      return "pricing-recommendation decrease";
    }

    return "pricing-recommendation maintain";
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
          <h1>
            Revenue Management Dashboard
          </h1>

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

      <motion.section
        className="pricing-recommendations-section"
        initial={{
          opacity: 0,
          y: 10,
        }}
        animate={{
          opacity: 1,
          y: 0,
        }}
        transition={{
          duration: 0.4,
          delay: 0.1,
        }}
      >

        <div className="pricing-recommendations-header">

          <div>
            <h2>
              Pricing Recommendations
            </h2>

            <p>
              Revenue management recommendations
              based on current hotel occupancy
              and pricing rules.
            </p>
          </div>

        </div>

        {actionMessage && (
          <div className="pricing-action-message">
            {actionMessage}
          </div>
        )}

        {pricingLoading ? (

          <div className="pricing-recommendations-loading">
            Loading pricing recommendations...
          </div>

        ) : pricingError ? (

          <div className="pricing-recommendations-error">
            <p>{pricingError}</p>
          </div>

        ) : pricingRecommendations.length === 0 ? (

          <div className="pricing-recommendations-empty">
            <p>
              No pricing recommendations
              available.
            </p>
          </div>

        ) : (

          <div className="pricing-recommendations-table-wrapper">

            <table className="pricing-recommendations-table">

              <thead>
                <tr>
                  <th>Room</th>
                  <th>Current Price</th>
                  <th>Occupancy</th>
                  <th>Adjustment</th>
                  <th>Recommended Price</th>
                  <th>Recommendation</th>
                  <th>Reason</th>
                  <th>Actions</th>
                </tr>
              </thead>

              <tbody>

                {pricingRecommendations.map(
                  (recommendation) => (

                    <tr
                      key={
                        recommendation.roomId
                      }
                    >

                      <td>
                        <strong>
                          Room{" "}
                          {
                            recommendation.roomNumber
                          }
                        </strong>
                      </td>

                      <td>
                        {formatCurrency(
                          recommendation.currentPrice
                        )}
                      </td>

                      <td>
                        {
                          recommendation.occupancyRate
                        }%
                      </td>

                      <td>
                        {recommendation.adjustmentPercentage >
                        0
                          ? `+${recommendation.adjustmentPercentage}%`
                          : `${recommendation.adjustmentPercentage}%`}
                      </td>

                      <td>
                        <strong>
                          {formatCurrency(
                            recommendation.recommendedPrice
                          )}
                        </strong>
                      </td>

                      <td>

                        <span
                          className={getRecommendationClass(
                            recommendation.recommendation
                          )}
                        >

                          {getRecommendationIcon(
                            recommendation.recommendation
                          )}

                          <span>
                            {
                              recommendation.recommendation
                            }
                          </span>

                        </span>

                      </td>

                      <td>
                        {
                          recommendation.reason
                        }
                      </td>

                      <td>

                        <div className="pricing-actions">

                          <button
                            type="button"
                            className="pricing-accept-button"
                            disabled={
                              updatingRoomId ===
                              recommendation.roomId
                            }
                            onClick={() =>
                              handleAcceptRecommendation(
                                recommendation
                              )
                            }
                          >

                            <Check size={15} />

                            {updatingRoomId ===
                            recommendation.roomId
                              ? "Updating..."
                              : "Accept"}

                          </button>

                          <button
                            type="button"
                            className="pricing-reject-button"
                            disabled={
                              updatingRoomId ===
                              recommendation.roomId
                            }
                            onClick={() =>
                              handleRejectRecommendation(
                                recommendation
                              )
                            }
                          >

                            <X size={15} />

                            Reject

                          </button>

                        </div>

                      </td>

                    </tr>

                  )
                )}

              </tbody>

            </table>

          </div>

        )}

      </motion.section>

    </div>
  );
}

export default Dashboard;