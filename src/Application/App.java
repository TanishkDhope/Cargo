package Application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws Exception {
         try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/car_rental";
            String username = "root";
            String password = "Dadu2468";

            Connection connection = DriverManager.getConnection(url, username, password);

            if (connection != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to connect to the database!");
            }

            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        // LandingPage.main(args);
        new LoginPage();
        // AdminDashboard.main(args);
        // BookingConfirmationPage.main(args);
        // Payment.main(args);
    }
}