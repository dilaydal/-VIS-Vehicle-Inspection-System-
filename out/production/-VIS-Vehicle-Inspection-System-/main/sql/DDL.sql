CREATE DATABASE IF NOT EXISTS VIS;
USE VIS;

DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS Customers;
DROP TABLE IF EXISTS Mechanics;
DROP TABLE IF EXISTS Managers;

CREATE TABLE Customers (
    customerID INT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(100),
    password VARCHAR(255),
    fullName VARCHAR(100)
);

CREATE TABLE Mechanics (
    mechanicID INT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(100),
    password VARCHAR(255),
    contactInfo VARCHAR(400)  
);

CREATE TABLE Managers (
    managerID INT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(100),
    password VARCHAR(255)  
);

CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mechanicID INT, 
    customerID INT, 
    customer_name VARCHAR(255) NOT NULL,
    vehicle_type VARCHAR(255) NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    inspection_status ENUM('Failed', 'Passed') NULL,
    FOREIGN KEY (mechanicID) REFERENCES Mechanics(mechanicID),
    FOREIGN KEY (customerID) REFERENCES Customers(customerID)
);

