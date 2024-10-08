package Application;
import java.sql.*;

public class DatabaseConnector {
    public static void main(String[] args) {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Replace with your database URL, username, and password
            String url = "jdbc:mysql://localhost:3306/car_rental";
            String username = "root";
            String password = "Dadu2468";

            // Establish the database connection
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check if the connection is successful
            if (connection != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to connect to the database!");
            }

            // Close the connection when finished
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}