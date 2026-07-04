package util;

/**
 * DBConnection.java
 * Manages database connections (placeholder for future DB integration).
 * Currently not in use; data is stored in-memory via repositories.
 */
public class DBConnection {

    private static DBConnection instance;
    private boolean connected;

    private DBConnection() {
        this.connected = false;
    }

    /**
     * Gets the singleton instance of DBConnection.
     *
     * @return The DBConnection instance.
     */
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    /**
     * Establishes a database connection.
     * (Placeholder for future implementation)
     *
     * @return true if connection is successful.
     */
    public boolean connect() {
        connected = true;
        System.out.println("Database connection established (placeholder).");
        return connected;
    }

    /**
     * Closes the database connection.
     * (Placeholder for future implementation)
     *
     * @return true if disconnection is successful.
     */
    public boolean disconnect() {
        connected = false;
        System.out.println("Database connection closed (placeholder).");
        return true;
    }

    /**
     * Checks if the connection is active.
     *
     * @return true if connected.
     */
    public boolean isConnected() {
        return connected;
    }
}
