import { ArrowUpRight } from "lucide-react";

const bookings = [
  {
    guest: "Rahul Sharma",
    room: "Deluxe 204",
    dates: "Oct 12 – Oct 15",
    amount: "₹15,000",
    status: "Confirmed",
  },
  {
    guest: "Sarah Wilson",
    room: "Suite 301",
    dates: "Oct 14 – Oct 17",
    amount: "₹24,000",
    status: "Confirmed",
  },
  {
    guest: "Arjun Mehta",
    room: "Double 105",
    dates: "Oct 18 – Oct 20",
    amount: "₹10,000",
    status: "Pending",
  },
];

function RecentBookings() {
  return (
    <div className="table-card">
      <div className="section-header">
        <div>
          <h3>Recent Bookings</h3>
          <p>Latest reservation activity</p>
        </div>

        <button className="text-button">
          View all
          <ArrowUpRight size={16} />
        </button>
      </div>

      <div className="booking-list">
        {bookings.map((booking) => (
          <div className="booking-row" key={`${booking.guest}-${booking.room}`}>
            <div className="booking-avatar">
              {booking.guest.charAt(0)}
            </div>

            <div className="booking-main">
              <strong>{booking.guest}</strong>
              <span>{booking.room}</span>
            </div>

            <div className="booking-dates">
              <span>{booking.dates}</span>
            </div>

            <div className="booking-amount">
              <strong>{booking.amount}</strong>

              <span
                className={
                  booking.status === "Confirmed"
                    ? "status-confirmed"
                    : "status-pending"
                }
              >
                {booking.status}
              </span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default RecentBookings;