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
}