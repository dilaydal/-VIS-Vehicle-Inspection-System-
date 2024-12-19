package main.java.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentModel {
    private Connection connection;

    public AppointmentModel(Connection connection) {
        this.connection = connection;
    }

    public ResultSet getAllAppointments() throws SQLException {
        String query = "SELECT * FROM appointments";
        return connection.createStatement().executeQuery(query);
    }

    public ResultSet getAllAppointmentsWithJoinCustomers() throws SQLException {
        String query = """
            SELECT a.*, c.fullName
            FROM Appointments a
            JOIN Customers c ON a.customer_name = c.userName;
        """;
        return connection.createStatement().executeQuery(query);
    }
}