package util;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    private static final String[] URLs = {
            "jdbc:mysql://127.0.0.1:3306/library_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
            "jdbc:mysql://localhost:3306/library_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
    };
    private static final String[] USERS = {"root"};
    private static final String[] PASSWORDS = {"", "password", "root"};
    private static final String[] DRIVER_NAMES = {"com.mysql.cj.jdbc.Driver", "com.mysql.jdbc.Driver"};
    private static final String[] DRIVER_JAR_PATHS = {
            "lib/mysql-connector-j-8.0.33.jar",
            "mysql-connector-j-8.0.33.jar",
            "lib/mysql-connector-java-8.0.33.jar"
    };

    private static Connection connection;
    private static boolean driverLoaded;

    // Get connection instance
    public static Connection getConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    return connection;
                }
            } catch (SQLException e) {
                connection = null;
            }
        }

        if (!ensureDriverLoaded()) {
            return null;
        }

        for (String url : URLs) {
            for (String user : USERS) {
                for (String password : PASSWORDS) {
                    try {
                        connection = DriverManager.getConnection(url, user, password);
                        System.out.println("✅ Database connected successfully!");
                        return connection;
                    } catch (SQLException ignored) {
                        // Try the next combination.
                    }
                }
            }
        }

        System.out.println("Database connection unavailable; using in-memory demo data instead.");
        return null;
    }

    private static boolean ensureDriverLoaded() {
        if (driverLoaded) {
            return true;
        }

        for (String driverName : DRIVER_NAMES) {
            try {
                Class.forName(driverName);
                driverLoaded = true;
                return true;
            } catch (ClassNotFoundException ignored) {
                // Try the next driver name.
            }
        }

        for (String jarPath : DRIVER_JAR_PATHS) {
            File jarFile = new File(jarPath);
            if (!jarFile.exists()) {
                jarFile = new File(System.getProperty("user.dir"), jarPath);
            }
            if (!jarFile.exists()) {
                continue;
            }

            try {
                URLClassLoader jarLoader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()}, ClassLoader.getSystemClassLoader());
                for (String driverName : DRIVER_NAMES) {
                    try {
                        Class.forName(driverName, true, jarLoader);
                        driverLoaded = true;
                        return true;
                    } catch (ClassNotFoundException ignored) {
                        // Try the next driver name.
                    }
                }
            } catch (Exception e) {
                System.out.println("Unable to load MySQL driver from " + jarFile.getAbsolutePath());
            }
        }

        System.out.println("MySQL JDBC driver not available; using in-memory demo data instead.");
        return false;
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
