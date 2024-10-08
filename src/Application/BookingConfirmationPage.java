package Application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class BookingConfirmationPage extends Application {
    private String carName;
    private String fuelType;
    private String carType;
    private String rentalPrice;
    private LocalDate startDate;
    private LocalDate endDate;
    private String carImagePath; // Variable to hold the image path

    // No-argument constructor
    public BookingConfirmationPage() {
        this.carName = "";
        this.fuelType = "";
        this.carType = "";
        this.rentalPrice = "0";
        this.startDate = LocalDate.now();
        this.endDate = LocalDate.now();
        this.carImagePath = "path/to/default/image.png"; // Default image path
    }

    // Parameterized constructor
    public BookingConfirmationPage(String carName, String fuelType, String carType, String rentalPrice, LocalDate startDate, LocalDate endDate, String carImagePath) {
        this.carName = carName;
        this.fuelType = fuelType;
        this.carType = carType;
        this.rentalPrice = rentalPrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.carImagePath = carImagePath; // Set the image path
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Booking Confirmation");

        // Main layout
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #ecf0f1;");

        // Title
        Label titleLabel = new Label("Booking Confirmation");
        titleLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 24px; -fx-font-weight: bold;");

        // Car Information
        Label carInfoLabel = new Label("Car: " + carName);
        Label fuelInfoLabel = new Label("Fuel Type: " + fuelType);
        Label typeInfoLabel = new Label("Car Type: " + carType);
        Label priceInfoLabel = new Label("Rental Price: $" + rentalPrice + " per day");

        // Date Information
        Label dateInfoLabel = new Label("Rental Period: " + startDate + " to " + endDate);

        // Calculate total price
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1; // Including start date
        double totalPrice = daysBetween * Double.parseDouble(rentalPrice);
        Label totalPriceLabel = new Label("Total Price: $" + String.format("%.2f", totalPrice));
        totalPriceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Load car image
        Image carImage = new Image(carImagePath); // Load the image from the specified path
        ImageView carImageView = new ImageView(carImage);
        carImageView.setFitHeight(200); // Set desired height
        carImageView.setFitWidth(300); // Set desired width
        carImageView.setPreserveRatio(true); // Maintain aspect ratio

        // Pay Now button
        Button payNowButton = new Button("Pay Now");
        payNowButton.setStyle("-fx-background-color: #6c5ce7; -fx-text-fill: white; -fx-padding: 10px 20px; " +
                "-fx-border-radius: 20px; -fx-background-radius: 20px;");
        payNowButton.setOnAction(e -> {
            System.out.println("Payment successful for: " + carName);
            Payment paymentPage = new Payment();
            paymentPage.start(primaryStage);
    
        });

        // Adding all elements to the layout
        layout.getChildren().addAll(titleLabel, carImageView, carInfoLabel, fuelInfoLabel, typeInfoLabel, priceInfoLabel, dateInfoLabel, totalPriceLabel, payNowButton);
        layout.setAlignment(Pos.CENTER);

        // Create the scene and set it on the primary stage
        Scene scene = new Scene(layout, 600, 600); // Increased height to fit the image
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
