package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/library_db"; 
    private static final String USER = "root";   // change to your DB username
    private static final String PASSWORD = "password"; // change to your DB password

    private static Connection connection;

    // Get connection instance
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Database connected successfully!");
            } catch (ClassNotFoundException e) {
                System.out.println("MySQL JDBC driver not available; using in-memory demo data instead.");
            } catch (SQLException e) {
                System.out.println("Database connection unavailable; using in-memory demo data instead.");
            }
        }
        return connection;
    }

    // Close connection
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔒 Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
