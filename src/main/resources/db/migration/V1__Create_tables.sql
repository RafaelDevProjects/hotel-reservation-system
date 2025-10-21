CREATE TABLE rooms (
    id VARCHAR(36) PRIMARY KEY,
    number INT NOT NULL UNIQUE,
    type VARCHAR(20) NOT NULL,
    capacity INT NOT NULL,
    price_per_night DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE reservations (
    id VARCHAR(36) PRIMARY KEY,
    room_id VARCHAR(36) NOT NULL,
    guest_name VARCHAR(120) NOT NULL,
    checkin_expected DATE NOT NULL,
    checkout_expected DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    total_amount DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reservation_room FOREIGN KEY (room_id) REFERENCES rooms(id)
);

CREATE INDEX idx_reservations_room_dates ON reservations(room_id, checkin_expected, checkout_expected);
CREATE INDEX idx_reservations_status ON reservations(status);