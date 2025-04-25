package salon_appointment;
import java.sql.*;

public class JDBCconnection {
    public static Connection getConnection() throws SQLException, java.sql.SQLException {
        String url = "jdbc:mysql://localhost:3306/salon_db";
        String user = "root"; // change if needed
        String password = "MYSQL"; // set your MySQL password
        return DriverManager.getConnection(url, user, password);
    }
}
