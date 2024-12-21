use VIS;

INSERT INTO Customers ( userName, password, fullName) VALUES
( 'custom1', '0','custom1'),
( 'custom2', '0','cumstom2');

INSERT INTO Mechanics (mechanicID, userName, password) VALUES
(1, 'mech1', '0'),
(2, 'mech2', '0');

INSERT INTO Managers (managerID, userName, password) VALUES
(1, 'Ned Stark', '0'),
(2, 'Rob Stark', '0');

INSERT INTO appointments(mechanicID,customerID,customer_name,vehicle_type,appointment_date,appointment_time,inspection_status) VALUES
(1,1,'custom1', 'SUV', '2024-12-25', '10:00:00', 'failed' ),
(2,2,'custom2', 'Sedan', '2024-12-26', '14:00:00', 'failed' )


