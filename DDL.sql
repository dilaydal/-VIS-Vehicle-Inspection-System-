CREATE DATABASE IF NOT EXISTS VIS;
USE VIS;

DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS Mechanic;
DROP TABLE IF EXISTS Menager;

CREATE TABLE Customer (
    customerID INT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(100),
    password VARCHAR(255)  
);

CREATE TABLE Mechanic (
    mechanicID INT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(100),
    password VARCHAR(255)  
);

CREATE TABLE Menager (
    managerID INT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(100),
    password VARCHAR(255)  
);
