package model;

import utils.AuthResult;
import java.sql.*;

public class AuthenticationModel {
    public AuthResult authenticateUser(String username, String password) {
        try (Connection connection = DatabaseConnection.connect()) {
            int userId;
            //3 farklı user 3 farklı tableımız var her biri için ayrı ayrı kontrol ediyor
            //username unique olduğu için conflicte neden olmayacak
            userId = checkUser(connection, "Customers", "customerID", username, password);
            if (userId != -1) return new AuthResult("customer", userId, username);

            userId = checkUser(connection, "Mechanics", "mechanicID", username, password);
            if (userId != -1) return new AuthResult("mechanic", userId, username);

            userId = checkUser(connection, "Managers", "managerID", username, password);
            if (userId != -1) return new AuthResult("manager", userId, username);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int checkUser(Connection conn, String tableName, String idColumnName, String username, String password) throws SQLException {
        String sql = "SELECT " + idColumnName + " FROM " + tableName + " WHERE userName = ? AND password = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getInt(idColumnName);
                } else {
                    return -1;
                }
            }
        }
    }
}