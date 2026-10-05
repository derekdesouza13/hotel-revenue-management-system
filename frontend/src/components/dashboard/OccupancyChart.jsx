import {
  ResponsiveContainer,
  AreaChart,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
} from "recharts";

function OccupancyChart({ data = [] }) {
  const chartData = data.map((item) => ({
    date: item.date,
    occupancy: item.occupancyRate,
  }));

  return (
    <div className="chart-card">
      <div className="chart-header">
        <div>
          <h3>Occupancy</h3>
          <p>Daily room occupancy</p>
        </div>

        <span className="chart-period">This month</span>
      </div>

      <div className="chart-container">
        <ResponsiveContainer width="100%" height="100%">
          <AreaChart data={chartData}>
            <defs>
              <linearGradient
                id="occupancyGradient"
                x1="0"
                y1="0"
                x2="0"
                y2="1"
              >
                <stop
                  offset="0%"
                  stopColor="#10b981"
                  stopOpacity={0.25}
                />
                <stop
                  offset="100%"
                  stopColor="#10b981"
                  stopOpacity={0}
                />
              </linearGradient>
            </defs>

            <CartesianGrid
              strokeDasharray="3 3"
              vertical={false}
              stroke="#e5e7eb"
            />

            <XAxis
              dataKey="date"
              axisLine={false}
              tickLine={false}
              tick={{ fill: "#6b7280", fontSize: 12 }}
              tickFormatter={(value) =>
                new Date(value).getDate()
              }
            />

            <YAxis
              domain={[0, 100]}
              axisLine={false}
              tickLine={false}
              tick={{ fill: "#6b7280", fontSize: 12 }}
              tickFormatter={(value) => `${value}%`}
            />

            <Tooltip
              labelFormatter={(value) =>
                new Date(value).toLocaleDateString(
                  "en-IN",
                  {
                    day: "2-digit",
                    month: "short",
                  }
                )
              }
              formatter={(value) => [
                `${value}%`,
                "Occupancy",
              ]}
            />

            <Area
              type="monotone"
              dataKey="occupancy"
              stroke="#10b981"
              strokeWidth={3}
              fill="url(#occupancyGradient)"
            />
          </AreaChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}

export default OccupancyChart;