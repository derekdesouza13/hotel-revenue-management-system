import { AlertTriangle, ArrowUpRight, TrendingUp } from "lucide-react";

const alerts = [
  {
    title: "High occupancy detected",
    description: "Weekend occupancy has crossed 85%.",
    action: "+10% price",
    type: "warning",
  },
  {
    title: "Revenue opportunity",
    description: "Deluxe rooms have strong demand.",
    action: "+8% price",
    type: "success",
  },
  {
    title: "Low weekday demand",
    description: "Monday occupancy is below target.",
    action: "Review pricing",
    type: "neutral",
  },
];

function PricingAlerts() {
  return (
    <div className="alerts-card">
      <div className="section-header">
        <div>
          <h3>Pricing Insights</h3>
          <p>Revenue management recommendations</p>
        </div>
      </div>

      <div className="alert-list">
        {alerts.map((alert) => {
          const Icon =
            alert.type === "warning"
              ? AlertTriangle
              : alert.type === "success"
                ? TrendingUp
                : ArrowUpRight;

          return (
            <div className="alert-item" key={alert.title}>
              <div className={`alert-icon ${alert.type}`}>
                <Icon size={18} />
              </div>

              <div className="alert-content">
                <strong>{alert.title}</strong>
                <span>{alert.description}</span>
              </div>

              <button className="alert-action">
                {alert.action}
              </button>
            </div>
          );
        })}
      </div>
    </div>
  );
}

export default PricingAlerts;