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
import java.time.temporal.ChronoUnit;

public class LandingPage extends Application {
    private static String userName;
      String[][] carData = {
        { "Tesla Model S", "Electric", "Sedan", "/resources/car1.png", "300", "5" },
        { "Ford Mustang", "Petrol", "Coupe", "/resources/car2.png", "200", "4" },
        { "Toyota Prius", "Hybrid", "Hatchback", "/resources/car3.png", "150", "5" },
        { "Honda Civic", "Petrol", "Sedan", "/resources/car4.png", "180", "5" },
        { "BMW X5", "Diesel", "SUV", "/resources/car5.png", "400", "7" },
        { "Audi A6", "Petrol", "Sedan", "/resources/car6.png", "350", "5" },
};
    public static void setUserName(String userName) {
        LandingPage.userName = userName;
    }


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CarGo - Car Rental Service");

        HBox navbar = new HBox(20);
        navbar.setPadding(new Insets(20));
        navbar.setStyle("-fx-background-color: #2c3e50;");

        Label logo = new Label("CarGo");
        logo.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 28px; -fx-text-fill: white; -fx-font-weight: bold;");

        TextField searchField = new TextField();
        searchField.setPromptText("Search cars...");
        searchField.setPrefWidth(200);
        searchField.setStyle("-fx-padding: 10px; -fx-border-radius: 20px; -fx-background-radius: 20px; " +
                "-fx-border-color: #74b9ff; -fx-border-width: 1px;");

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #0984e3; -fx-text-fill: white; -fx-padding: 10px 20px; " +
                "-fx-border-radius: 20px; -fx-background-radius: 20px;");
        Label userNameLabel = new Label("Hello, " + userName + "!");
        userNameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 0 10px;");

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10px 20px; " +
                "-fx-border-radius: 20px; -fx-background-radius: 20px;");

        logoutButton.setOnAction(e -> {
            System.out.println("Logging out...");
            primaryStage.close();
            new LoginPage();
        });

        HBox.setHgrow(searchField, Priority.ALWAYS);
        navbar.getChildren().addAll(logo, searchField, searchButton, userNameLabel, logoutButton);
        navbar.setAlignment(Pos.CENTER_LEFT);

        HBox mainLayout = new HBox(10);
        mainLayout.setPadding(new Insets(10));

        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: linear-gradient(to bottom right, #3d4c56, #34495e);");
        sidebar.setPrefWidth(250);
        sidebar.setMinWidth(200);
        sidebar.setMaxWidth(300);
        sidebar.setEffect(new javafx.scene.effect.DropShadow(10, javafx.scene.paint.Color.BLACK));

        Label priceLabel = new Label("Price Range:");
        priceLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Slider priceSlider = new Slider(0, 500, 250);
        priceSlider.setShowTickLabels(true);
        priceSlider.setShowTickMarks(true);
        priceSlider.setMajorTickUnit(100);
        priceSlider.setBlockIncrement(10);
        priceSlider.setMinorTickCount(4);
        priceSlider.setStyle("-fx-tick-label-fill: white; -fx-tick-mark-fill: white;");

        Label selectedPriceLabel = new Label("Selected Price: $250");
        selectedPriceLabel
                .setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 10px 0;");

        priceSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            selectedPriceLabel.setText("Selected Price: $" + String.format("%.0f", newVal.doubleValue()));
        });

        Label fuelTypeLabel = new Label("Fuel Type:");
        fuelTypeLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        ComboBox<String> fuelTypeCombo = new ComboBox<>();
        fuelTypeCombo.getItems().addAll("All", "Petrol", "Diesel", "Electric", "Hybrid");
        fuelTypeCombo.setValue("All");

        Label carTypeLabelLabel = new Label("Car Type:");
        carTypeLabelLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        ComboBox<String> carTypeCombo = new ComboBox<>();
        carTypeCombo.getItems().addAll("All", "Sedan", "Coupe", "Hatchback", "SUV");
        carTypeCombo.setValue("All");

        Label seaterLabel = new Label("Car Seater:");
        seaterLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        ComboBox<String> seaterCombo = new ComboBox<>();
        seaterCombo.getItems().addAll("All", "2", "4", "5", "7", "8");
        seaterCombo.setValue("All");

        sidebar.getChildren().addAll(priceLabel, priceSlider, selectedPriceLabel, fuelTypeLabel, fuelTypeCombo,
                carTypeLabelLabel, carTypeCombo, seaterLabel, seaterCombo);
        sidebar.setAlignment(Pos.TOP_LEFT);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #ecf0f1;");

        TilePane carListings = new TilePane();
        carListings.setPadding(new Insets(20));
        carListings.setHgap(20);
        carListings.setVgap(20);
        carListings.setPrefColumns(3);
        carListings.setStyle("-fx-background-color: #ecf0f1;");

      

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filterCarListings(carListings, newVal);
        });


        for (String[] car : carData) {
            carListings.getChildren().add(createCarCard(car[0], car[1], car[2], car[3], car[4], car[5]));
        }


        scrollPane.setContent(carListings);

        mainLayout.getChildren().addAll(sidebar, scrollPane);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        VBox layout = new VBox(navbar, mainLayout);
        Scene scene = new Scene(layout, 1200, 600);

        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() < 800) {
                sidebar.setPrefWidth(newVal.doubleValue() / 4);
            } else {
                sidebar.setPrefWidth(250);
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createCarCard(String carName, String fuelType, String carType, String imagePath, String rentalPrice,
            String seaterCount) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 15px;" +
                        "-fx-border-radius: 15px;" +
                        "-fx-border-color: #dfe6e9;" +
                        "-fx-border-width: 1px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 10, 0, 0, 0);");
        card.setPrefSize(300, 350);

        ImageView carImage;
        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            carImage = new ImageView(image);
        } catch (Exception e) {
            carImage = new ImageView(); 
        }
        carImage.setFitHeight(200);
        carImage.setFitWidth(250);

        Label carNameLabel = new Label(carName);
        carNameLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 18px; -fx-font-weight: bold;");

        Label fuelTypeLabel = new Label("Fuel: " + fuelType);
        fuelTypeLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-text-fill: #636e72;");

        Label carTypeLabelLabel = new Label("Type: " + carType);
        carTypeLabelLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-text-fill: #636e72;");

        Label rentalPriceLabel = new Label("Rental Price: $" + rentalPrice + " per day");
        rentalPriceLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-text-fill: #d63031;");

        Label seaterLabel = new Label("Seats: " + seaterCount);
        seaterLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-text-fill: #636e72;");

        Button bookNowButton = new Button("Book Now");
        bookNowButton.setStyle(
                "-fx-background-color: #0984e3; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        bookNowButton.setOnAction(e -> {
            Stage bookingStage = new Stage();
            bookingStage.setTitle("Book Car - " + carName);

            VBox bookingLayout = new VBox(10);
            bookingLayout.setPadding(new Insets(20));

            Label startDateLabel = new Label("Starting Date:");
            DatePicker startDatePicker = new DatePicker();

            Label endDateLabel = new Label("Ending Date:");
            DatePicker endDatePicker = new DatePicker();

            Label totalPriceLabel = new Label("Total Price: $0.00");
            totalPriceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            startDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> updateTotalPrice(startDatePicker,
                    endDatePicker, rentalPrice, totalPriceLabel));
            endDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> updateTotalPrice(startDatePicker,
                    endDatePicker, rentalPrice, totalPriceLabel));

            Button confirmButton = new Button("Confirm Booking");
            confirmButton.setStyle(
                    "-fx-background-color: #6c5ce7; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-border-radius: 20px; -fx-background-radius: 20px;");
            confirmButton.setOnAction(bookingEvent -> {
                LocalDate startDate = startDatePicker.getValue();
                LocalDate endDate = endDatePicker.getValue();
            
                if (startDate == null || endDate == null) {
                    showErrorPopup("Error", "Please select both start and end dates.");
                } else if (startDate.isAfter(endDate)) {
                    showErrorPopup("Date Error", "Start date cannot be later than end date.");
                } else {
                   
                    BookingConfirmationPage bookingConfirmationPage = new BookingConfirmationPage(
                            carName,
                            fuelType,
                            carType,
                            rentalPrice,
                            startDate,
                            endDate,
                            imagePath 
                    );
            
                    bookingConfirmationPage.start(new Stage()); 
                    bookingStage.close(); 
                }
            });

            bookingLayout.getChildren().addAll(startDateLabel, startDatePicker, endDateLabel, endDatePicker,
                    totalPriceLabel, confirmButton);
            bookingLayout.setAlignment(Pos.CENTER_LEFT);

            Scene bookingScene = new Scene(bookingLayout, 300, 250);
            bookingStage.setScene(bookingScene);
            bookingStage.show();
        });

        card.getChildren().addAll(carImage, carNameLabel, fuelTypeLabel, carTypeLabelLabel, rentalPriceLabel,
                seaterLabel, bookNowButton);
        card.setAlignment(Pos.CENTER);
        return card;
    }

    private void updateTotalPrice(DatePicker startDatePicker, DatePicker endDatePicker, String rentalPrice,
            Label totalPriceLabel) {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null && !endDate.isBefore(startDate)) {
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1; // Including the start day
            double pricePerDay = Double.parseDouble(rentalPrice);
            double totalPrice = daysBetween * pricePerDay;
            totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice));
        } else {
            totalPriceLabel.setText("Total Price: $0.00");
        }
    }
private void showErrorPopup(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}
private void filterCarListings(TilePane carListings, String query) {
    carListings.getChildren().clear(); 

    for (String[] car : carData) {
        boolean matchFound = false;

        if (car[0].toLowerCase().contains(query.toLowerCase())) {
            matchFound = true;
        } else if (car[1].toLowerCase().contains(query.toLowerCase())) {
            matchFound = true;
        } else if (car[2].toLowerCase().contains(query.toLowerCase())) {
            matchFound = true;
        }
        if (matchFound) {
            carListings.getChildren().add(createCarCard(car[0], car[1], car[2], car[3], car[4], car[5]));
        }
    }

    if (carListings.getChildren().isEmpty()) {
        Label noResults = new Label("No cars found matching your search.");
        noResults.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        carListings.getChildren().add(noResults);
    }
}

    public static void main(String[] args) {
        launch(args);
    }
}