import { useEffect, useState } from "react";
import {
  AlertTriangle,
  ArrowUpRight,
  TrendingUp,
} from "lucide-react";

import { getPricingRules } from "../../api/pricingRules";

function PricingAlerts() {
  const [rules, setRules] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchPricingRules = async () => {
      try {
        const data = await getPricingRules();

        setRules(data);
      } catch (error) {
        console.error(
          "Failed to load pricing rules:",
          error
        );
      } finally {
        setLoading(false);
      }
    };

    fetchPricingRules();
  }, []);

  const getRuleIcon = (adjustment) => {
    if (adjustment > 0) {
      return TrendingUp;
    }

    if (adjustment < 0) {
      return AlertTriangle;
    }

    return ArrowUpRight;
  };

  const getRuleType = (adjustment) => {
    if (adjustment > 0) {
      return "success";
    }

    if (adjustment < 0) {
      return "warning";
    }

    return "neutral";
  };

  const formatAdjustment = (adjustment) => {
    const value = Number(adjustment || 0);

    if (value > 0) {
      return `+${value}%`;
    }

    return `${value}%`;
  };

  if (loading) {
    return (
      <div className="alerts-card">
        <div className="section-header">
          <div>
            <h3>Pricing Insights</h3>
            <p>
              Revenue management recommendations
            </p>
          </div>
        </div>

        <div className="alert-list">
          <div className="alert-item">
            <div className="alert-content">
              <span>Loading pricing rules...</span>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="alerts-card">
      <div className="section-header">
        <div>
          <h3>Pricing Insights</h3>
          <p>
            Active revenue management rules
          </p>
        </div>
      </div>

      <div className="alert-list">
        {rules.length === 0 ? (
          <div className="alert-item">
            <div className="alert-content">
              <strong>
                No pricing rules configured
              </strong>

              <span>
                Create a pricing rule to enable
                automated revenue recommendations.
              </span>
            </div>
          </div>
        ) : (
          rules.map((rule) => {
            const adjustment =
              Number(rule.adjustmentPercentage || 0);

            const Icon = getRuleIcon(adjustment);
            const type = getRuleType(adjustment);

            return (
              <div
                className="alert-item"
                key={rule.id}
              >
                <div
                  className={`alert-icon ${type}`}
                >
                  <Icon size={18} />
                </div>

                <div className="alert-content">
                  <strong>
                    {rule.ruleName}
                  </strong>

                  <span>
                    Trigger at{" "}
                    {rule.occupancyThreshold}% occupancy.
                    {" "}
                    {rule.active
                      ? "Rule is active."
                      : "Rule is inactive."}
                  </span>
                </div>

                <button className="alert-action">
                  {formatAdjustment(
                    adjustment
                  )}
                </button>
              </div>
            );
          })
        )}
      </div>
    </div>
  );
}

export default PricingAlerts;