import { NavLink } from "react-router-dom";
import {
  BarChart3,
  Building2,
  BedDouble,
  Users,
  CalendarCheck,
  Tags,
} from "lucide-react";

const navigationItems = [
  {
    name: "Dashboard",
    path: "/",
    icon: BarChart3,
  },
  {
    name: "Hotels",
    path: "/hotels",
    icon: Building2,
  },
  {
    name: "Rooms",
    path: "/rooms",
    icon: BedDouble,
  },
  {
    name: "Guests",
    path: "/guests",
    icon: Users,
  },
  {
    name: "Bookings",
    path: "/bookings",
    icon: CalendarCheck,
  },
  {
    name: "Pricing Rules",
    path: "/pricing-rules",
    icon: Tags,
  },
];

function Sidebar() {
  return (
    <aside className="sidebar">
      <div className="sidebar-brand">
        <div className="brand-mark">HR</div>

        <div>
          <h2>Hotel Revenue</h2>
          <span>Management System</span>
        </div>
      </div>

      <nav className="sidebar-nav">
        {navigationItems.map((item) => {
          const Icon = item.icon;

          return (
            <NavLink
              key={item.path}
              to={item.path}
              end={item.path === "/"}
              className={({ isActive }) =>
                `nav-item ${isActive ? "active" : ""}`
              }
            >
              <Icon size={19} />
              <span>{item.name}</span>
            </NavLink>
          );
        })}
      </nav>
    </aside>
  );
}

export default Sidebar;