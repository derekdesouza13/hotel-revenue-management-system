import {
  ResponsiveContainer,
  AreaChart,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
} from "recharts";

const data = [
  { day: "Mon", occupancy: 68 },
  { day: "Tue", occupancy: 72 },
  { day: "Wed", occupancy: 75 },
  { day: "Thu", occupancy: 71 },
  { day: "Fri", occupancy: 84 },
  { day: "Sat", occupancy: 91 },
  { day: "Sun", occupancy: 87 },
];

function OccupancyChart() {
  return (
    <div className="chart-card">
      <div className="chart-header">
        <div>
          <h3>Occupancy</h3>
          <p>Average room occupancy</p>
        </div>

        <span className="chart-period">This week</span>
      </div>

      <div className="chart-container">
        <ResponsiveContainer width="100%" height="100%">
          <AreaChart data={data}>
            <defs>
              <linearGradient
                id="occupancyGradient"
                x1="0"
                y1="0"
                x2="0"
                y2="1"
              >
                <stop offset="0%" stopColor="#10b981" stopOpacity={0.25} />
                <stop offset="100%" stopColor="#10b981" stopOpacity={0} />
              </linearGradient>
            </defs>

            <CartesianGrid
              strokeDasharray="3 3"
              vertical={false}
              stroke="#e5e7eb"
            />

            <XAxis
              dataKey="day"
              axisLine={false}
              tickLine={false}
              tick={{ fill: "#6b7280", fontSize: 12 }}
            />

            <YAxis
              domain={[0, 100]}
              axisLine={false}
              tickLine={false}
              tick={{ fill: "#6b7280", fontSize: 12 }}
              tickFormatter={(value) => `${value}%`}
            />

            <Tooltip
              formatter={(value) => [`${value}%`, "Occupancy"]}
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