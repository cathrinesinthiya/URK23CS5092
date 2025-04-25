package salon_appointment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/salon_db"; // Ensure port and DB name are correct
        String username = "root";
        String password = "MYSQL"; // Replace with your actual MySQL root password

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Optional in newer JDBC versions, but good practice
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            throw e;
        }
    }
}
