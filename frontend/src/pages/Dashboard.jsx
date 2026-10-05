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

function Dashboard() {
  return (
    <div className="dashboard">
      <motion.div
        className="dashboard-intro"
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
      >
        <div>
          <p className="eyebrow">OVERVIEW</p>
          <h1>Good evening, Revenue Manager</h1>
          <p>
            Here's what's happening across your hotel operations today.
          </p>
        </div>

        <div className="dashboard-date">
          <span>Performance period</span>
          <strong>October 2026</strong>
        </div>
      </motion.div>

      <div className="stats-grid">
        <StatCard
          title="Total Revenue"
          value="₹3.10L"
          change="12.5%"
          subtitle="vs last month"
          icon={IndianRupee}
        />

        <StatCard
          title="Occupancy"
          value="78.4%"
          change="5.2%"
          subtitle="vs last month"
          icon={BedDouble}
        />

        <StatCard
          title="ADR"
          value="₹6,240"
          change="8.1%"
          subtitle="vs last month"
          icon={TrendingUp}
        />

        <StatCard
          title="Bookings"
          value="142"
          change="18.7%"
          subtitle="vs last month"
          icon={CalendarCheck}
        />
      </div>

      <div className="charts-grid">
        <RevenueChart />
        <OccupancyChart />
      </div>

      <div className="bottom-grid">
        <RecentBookings />
        <PricingAlerts />
      </div>
    </div>
  );
}

export default Dashboard;