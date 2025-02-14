package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {
    private static DataSource instance;
    private Connection con;
    private final String URL = "jdbc:mysql://localhost:3306/eventmanagement"; // ✅ Fixed typo
    private final String USER = "root";
    private final String PASSWORD = "root";

    private DataSource() {
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Database connection established successfully.");

            // 🔥 Register a shutdown hook to close connection on app exit
            Runtime.getRuntime().addShutdownHook(new Thread(this::closeConnection));

        } catch (SQLException e) {
            throw new RuntimeException("❌ Failed to establish database connection!", e);
        }
    }

    public static DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }
        return instance;
    }

    public Connection getCon() {
        try {
            if (con == null || con.isClosed()) {
                System.out.println("⚠️ Connection was closed. Reconnecting...");
                con = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Failed to reconnect to the database!", e);
        }
        return con;
    }

    public void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("🔌 Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error closing database connection: " + e.getMessage());
        }
    }
}
