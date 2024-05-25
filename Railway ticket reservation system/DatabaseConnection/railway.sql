CREATE DATABASE railway_reservation;

USE railway_reservation;

CREATE TABLE tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    passenger_name VARCHAR(100) NOT NULL,
    train_number VARCHAR(10) NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    journey_date DATE NOT NULL
);
