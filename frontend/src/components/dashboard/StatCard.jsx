import { motion } from "framer-motion";

function StatCard({
  title,
  value,
  change,
  subtitle,
  icon: Icon,
  positive = true,
}) {
  return (
    <motion.div
      className="stat-card"
      initial={{ opacity: 0, y: 15 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.4 }}
      whileHover={{ y: -4 }}
    >
      <div className="stat-card-top">
        <div>
          <p className="stat-title">{title}</p>
          <h3>{value}</h3>
        </div>

        <div className="stat-icon">
          <Icon size={20} />
        </div>
      </div>

      <div className="stat-footer">
        <span className={positive ? "positive-change" : "negative-change"}>
          {positive ? "↑" : "↓"} {change}
        </span>

        <span>{subtitle}</span>
      </div>
    </motion.div>
  );
}

export default StatCard;