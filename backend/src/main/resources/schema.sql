CREATE TABLE IF NOT EXISTS hotels (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    location VARCHAR(150) NOT NULL,
    total_rooms INT NOT NULL,
    created_at DATETIME NOT NULL,

    PRIMARY KEY (id),

    INDEX idx_hotel_location (location)
);


CREATE TABLE IF NOT EXISTS guests (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(30),

    PRIMARY KEY (id),

    UNIQUE KEY uk_guest_email (email),

    INDEX idx_guest_email (email)
);


CREATE TABLE IF NOT EXISTS rooms (
    id BIGINT NOT NULL AUTO_INCREMENT,
    hotel_id BIGINT NOT NULL,
    room_number VARCHAR(20) NOT NULL,
    room_type VARCHAR(30) NOT NULL,
    base_price DECIMAL(12,2) NOT NULL,
    status VARCHAR(30) NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_room_hotel
        FOREIGN KEY (hotel_id)
        REFERENCES hotels(id),

    CONSTRAINT uk_room_hotel_number
        UNIQUE (hotel_id, room_number),

    INDEX idx_room_hotel (hotel_id),
    INDEX idx_room_status (status)
);


CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT NOT NULL AUTO_INCREMENT,
    hotel_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    guest_id BIGINT NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    booking_date DATETIME NOT NULL,
    status VARCHAR(30) NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_booking_hotel
        FOREIGN KEY (hotel_id)
        REFERENCES hotels(id),

    CONSTRAINT fk_booking_room
        FOREIGN KEY (room_id)
        REFERENCES rooms(id),

    CONSTRAINT fk_booking_guest
        FOREIGN KEY (guest_id)
        REFERENCES guests(id),

    INDEX idx_booking_room (room_id),
    INDEX idx_booking_hotel (hotel_id),
    INDEX idx_booking_status (status),
    INDEX idx_booking_dates (check_in, check_out)
);


CREATE TABLE IF NOT EXISTS pricing_rules (
    id BIGINT NOT NULL AUTO_INCREMENT,
    hotel_id BIGINT NOT NULL,
    rule_name VARCHAR(100) NOT NULL,
    occupancy_threshold DECIMAL(5,2) NOT NULL,
    adjustment_percentage DECIMAL(6,2) NOT NULL,
    active BOOLEAN NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_pricing_rule_hotel
        FOREIGN KEY (hotel_id)
        REFERENCES hotels(id),

    INDEX idx_pricing_rule_hotel (hotel_id)
);


CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(80) NOT NULL,
    email VARCHAR(150) NOT NULL,
    role VARCHAR(30) NOT NULL,
    active BOOLEAN NOT NULL,

    PRIMARY KEY (id),

    UNIQUE KEY uk_user_username (username),
    UNIQUE KEY uk_user_email (email),

    INDEX idx_user_email (email)
);