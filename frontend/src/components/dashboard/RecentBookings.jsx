import { useEffect, useState } from "react";
import { ArrowUpRight } from "lucide-react";
import apiClient from "../../api/client";

function RecentBookings() {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchRecentBookings = async () => {
      try {
        const response = await apiClient.get("/analytics/dashboard");

        setBookings(response.data.recentBookings || []);
      } catch (error) {
        console.error(
          "Failed to load recent bookings:",
          error
        );
      } finally {
        setLoading(false);
      }
    };

    fetchRecentBookings();
  }, []);

  const formatDateRange = (checkIn, checkOut) => {
    const startDate = new Date(`${checkIn}T00:00:00`);
    const endDate = new Date(`${checkOut}T00:00:00`);

    const formatDate = (date) =>
      date.toLocaleDateString("en-IN", {
        day: "2-digit",
        month: "short",
      });

    return `${formatDate(startDate)} - ${formatDate(endDate)}`;
  };

  const formatAmount = (amount) => {
    return `₹${Number(amount || 0).toLocaleString("en-IN")}`;
  };

  const getStatusClass = (status) => {
    switch (status) {
      case "CONFIRMED":
        return "status-confirmed";

      case "CANCELLED":
        return "status-cancelled";

      default:
        return "status-pending";
    }
  };

  if (loading) {
    return (
      <div className="dashboard-card recent-bookings-card">
        <div className="card-header">
          <div>
            <h3>Recent Bookings</h3>
            <p>Latest reservations</p>
          </div>
        </div>

        <div className="empty-state">
          Loading bookings...
        </div>
      </div>
    );
  }

  return (
    <div className="dashboard-card recent-bookings-card">
      <div className="card-header">
        <div>
          <h3>Recent Bookings</h3>
          <p>Latest reservations</p>
        </div>

        <button className="card-action">
          View all
          <ArrowUpRight size={16} />
        </button>
      </div>

      <div className="recent-bookings-list">
        {bookings.length === 0 ? (
          <div className="empty-state">
            No recent bookings found.
          </div>
        ) : (
          bookings.map((booking) => (
            <div
              className="booking-row"
              key={booking.id}
            >
              <div className="booking-main">
                <div className="booking-avatar">
                  {booking.guestName
                    ? booking.guestName.charAt(0).toUpperCase()
                    : "G"}
                </div>

                <div className="booking-info">
                  <strong>
                    {booking.guestName}
                  </strong>

                  <span>
                    Room {booking.roomNumber}
                  </span>

                  <small>
                    {formatDateRange(
                      booking.checkIn,
                      booking.checkOut
                    )}
                  </small>
                </div>
              </div>

              <div className="booking-meta">
                <strong>
                  {formatAmount(
                    booking.totalAmount
                  )}
                </strong>

                <span
                  className={`booking-status ${getStatusClass(
                    booking.status
                  )}`}
                >
                  {booking.status}
                </span>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}

export default RecentBookings;