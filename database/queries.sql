-- ==========================================
-- View all hotels
-- ==========================================

SELECT *
FROM hotels;


-- ==========================================
-- View all rooms with hotel names
-- ==========================================

SELECT
    r.id,
    h.name AS hotel_name,
    r.room_number,
    r.room_type,
    r.base_price,
    r.status
FROM rooms r
JOIN hotels h
    ON r.hotel_id = h.id;


-- ==========================================
-- Find available rooms
-- ==========================================

SELECT
    r.*
FROM rooms r
WHERE r.status = 'AVAILABLE';


-- ==========================================
-- View bookings with guest and room details
-- ==========================================

SELECT
    b.id,
    h.name AS hotel_name,
    r.room_number,
    g.name AS guest_name,
    b.check_in,
    b.check_out,
    b.status,
    b.total_amount
FROM bookings b
JOIN hotels h
    ON b.hotel_id = h.id
JOIN rooms r
    ON b.room_id = r.id
JOIN guests g
    ON b.guest_id = g.id;


-- ==========================================
-- Calculate room counts by hotel
-- ==========================================

SELECT
    h.name,
    COUNT(r.id) AS room_count
FROM hotels h
LEFT JOIN rooms r
    ON h.id = r.hotel_id
GROUP BY h.id, h.name;


-- ==========================================
-- Calculate revenue by hotel
-- ==========================================

SELECT
    h.name,
    COALESCE(SUM(b.total_amount), 0) AS total_revenue
FROM hotels h
LEFT JOIN bookings b
    ON h.id = b.hotel_id
    AND b.status <> 'CANCELLED'
GROUP BY h.id, h.name;


-- ==========================================
-- Revenue by room type
-- ==========================================

SELECT
    r.room_type,
    SUM(b.total_amount) AS revenue
FROM bookings b
JOIN rooms r
    ON b.room_id = r.id
WHERE b.status <> 'CANCELLED'
GROUP BY r.room_type
ORDER BY revenue DESC;