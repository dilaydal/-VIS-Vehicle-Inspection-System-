package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MechanicModel {
    private Connection connection;

    public MechanicModel(Connection connection) {
        this.connection = connection;
    }

    public boolean addMechanic(String name, String password, String contactInfo) throws SQLException {
        String query = "INSERT INTO Mechanics (userName, password, contactInfo) VALUES (?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, name);
            st.setString(2, password);
            st.setString(3, contactInfo);
            return st.executeUpdate() > 0;
        }
    }

    public boolean removeMechanic(String name) throws SQLException {
        String query = "DELETE FROM Mechanics WHERE userName = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, name);
            return st.executeUpdate() > 0;
        }
    }

    public ResultSet getAllMechanics() throws SQLException {
        String query = "SELECT * FROM Mechanics";
        return connection.createStatement().executeQuery(query);
    }

    public ResultSet getDailyTaskSchedule(String mechanicName) throws SQLException {
        String query = "SELECT customer_name, vehicle_type, appointment_time FROM appointments WHERE mechanic_name = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, mechanicName);
        return statement.executeQuery();
    }

    public boolean updateInspectionStatus(String customerName, String vehicleType, String appointmentTime, String status) throws SQLException {
        String updateQuery = "UPDATE appointments SET inspection_status = ? WHERE customer_name = ? AND vehicle_type = ? AND appointment_time = ?";
        PreparedStatement statement = connection.prepareStatement(updateQuery);
        statement.setString(1, status);
        statement.setString(2, customerName);
        statement.setString(3, vehicleType);
        statement.setString(4, appointmentTime);

        return statement.executeUpdate() > 0;
    }
}