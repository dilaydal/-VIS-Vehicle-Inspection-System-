package main.java.model;

import java.sql.Connection;
import java.sql.DriverManager;
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

    public ResultSet getDailyTaskSchedule(int mechanicID) throws SQLException {
        String query = "SELECT id,customer_name, vehicle_type, appointment_time,inspection_status FROM appointments WHERE mechanicID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, mechanicID);
        return statement.executeQuery();
    }
    /*
     * public boolean updateInspectionStatus(int mechanicID, String customerName,
     * String vehicleType,
     * String appointmentTime, String status) throws SQLException {
     * String updateQuery = "UPDATE appointments " +
     * "SET inspection_status = ? " +
     * "WHERE mechanicID = ? AND customer_name = ? " +
     * "AND vehicle_type = ? AND appointment_time = ?";
     * PreparedStatement statement = connection.prepareStatement(updateQuery);
     * statement.setString(1, status); // inspection_status
     * statement.setInt(2, mechanicID); // mechanicID
     * 
     * statement.setString(3, customerName); // customer_name
     * statement.setString(4, vehicleType); // vehicle_type
     * 
     * statement.setString(5, appointmentTime); // appointment_time
     * return statement.executeUpdate() > 0;
     * }
     */

    public void updateInspectionStatus(int appointmentID, String newStatus) {
        String query = "UPDATE Appointments SET inspection_status = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, appointmentID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}