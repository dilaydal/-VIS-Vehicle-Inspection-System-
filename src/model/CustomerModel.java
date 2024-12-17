package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerModel {
    private Connection connection;

    public CustomerModel(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<String> getAppointments(String customerName) throws SQLException {
        String query = "SELECT id, appointment_date, appointment_time, vehicle_type FROM appointments WHERE customer_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, customerName);
            ResultSet resultSet = statement.executeQuery();

            ArrayList<String> appointments = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String date = resultSet.getString("appointment_date");
                String time = resultSet.getString("appointment_time");
                String vehicle = resultSet.getString("vehicle_type");

                appointments.add("ID: " + id + " | Date: " + date + " | Time: " + time + " | Vehicle: " + vehicle);
            }
            return appointments;
        }
    }

    public boolean rescheduleAppointment(int appointmentId, String newDate, String newTime) throws SQLException {
        String query = "UPDATE appointments SET appointment_date = ?, appointment_time = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newDate);
            statement.setString(2, newTime);
            statement.setInt(3, appointmentId);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean cancelAppointment(int appointmentId) throws SQLException {
        String query = "DELETE FROM appointments WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, appointmentId);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean createAppointment(String customerName, String vehicleType, String date, String time) throws SQLException {
        String query = "INSERT INTO appointments (customer_name, vehicle_type, appointment_date, appointment_time) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, customerName);
            statement.setString(2, vehicleType);
            statement.setString(3, date);
            statement.setString(4, time);

            return statement.executeUpdate() > 0;
        }
    }
}