package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {

    // Isko public static rakhenge taaki poore project mein ek hi connection share ho sake (Singleton pattern)
    public static Connection connection = null;

    public static Connection getConnection() {
        try {
            // Agar connection pehle se khula hai toh dobara nahi kholega
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");


                // Clever Cloud live configurations injected (As per your exact code)
                // ⚠️ FOR GITHUB PUBLISHING: REPLACE WITH YOUR ACTUAL CREDENTIALS OR USE ENVIRONMENT VARIABLES
                String host     = "YOUR_HOST_HERE"; 
                String dbName   = "YOUR_DB_NAME_HERE"; 
                String user     = "YOUR_USERNAME_HERE";

                // ⚠️ DASHBOARD SE REAL PASSWORD COPY KARKE YAHAN PASTE KARO:
                // ⚠️ FOR GITHUB PUBLISHING: REPLACE WITH YOUR ACTUAL PASSWORD OR USE ENVIRONMENT VARIABLES
                String password = "YOUR_PASSWORD_HERE"; 

                // 🚀 CRITICAL PATCH: Large .mp4 videos ko small network frames mein binary stream karne ke liye properties append ki hain
                String url = "jdbc:mysql://" + host + ":3306/" + dbName
                        + "?useSSL=false"
                        + "&allowPublicKeyRetrieval=true"
                        + "&useServerPrepStmts=false"       // Client side chunks handling (Clever Cloud packet restriction ko bypass karega)
                        + "&rewriteBatchedStatements=true" // Fast performance parameters for heavy data rows
                        + "&connectTimeout=30000"          // Slow internet par application timeout hone se bachayega (30s)
                        + "&socketTimeout=60000";          // Large media streaming ke waqt connection active rakhega (60s)

                connection = DriverManager.getConnection(url, user, password);

                System.out.println("✔ IDENTITY VERIFIED :: JDBC LINK ACTIVE :: CLOUD ACCESS AUTHORIZED");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("❌ ERROR: Could not connect to Clever Cloud remote host.");
            e.printStackTrace();
        }

        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✔ JDBC Session Terminated Safely.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Connection testConn = getConnection();
        if (testConn != null) {
            System.out.println("--- Cloud Connection Test Passed ---");
            closeConnection();
        } else {
            System.out.println("--- Cloud Connection Test Failed ---");
        }
    }
}