package Application;

import java.io.InputStream;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.control.ButtonBar.ButtonData;

public class AdminDashboard extends Application {

    private BorderPane mainLayout;
    private ObservableList<HBox> bookingCards;
    private ObservableList<HBox> fleetCards;
    private ObservableList<HBox> userCards;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CarGo Admin Dashboard");

        Label title = new Label("CarGo Admin Dashboard");
        title.setFont(new Font("Arial", 26));
        title.setStyle("-fx-text-fill: #27ae60;");

        Button logoutButton = new Button("Logout");
        styleButton(logoutButton, "#e74c3c", "#ffffff");
        logoutButton.setPrefWidth(80);
        logoutButton.setOnAction(e -> {
            System.out.println("Logging out...");
            primaryStage.close();
            new LoginPage();
        });

        HBox topBar = new HBox(title, logoutButton);
        topBar.setSpacing(20);
        topBar.setPadding(new Insets(20));
        topBar.setAlignment(Pos.CENTER);
        HBox.setHgrow(title, Priority.ALWAYS);

        Button bookingsButton = createNavButton("Manage Bookings");
        Button fleetButton = createNavButton("Manage Fleet");
        Button usersButton = createNavButton("Manage Users");
        Button analyticsButton = createNavButton("View Analytics");

        VBox sideMenu = new VBox(bookingsButton, fleetButton, usersButton, analyticsButton);
        sideMenu.setSpacing(15);
        sideMenu.setPadding(new Insets(20));
        sideMenu.setStyle("-fx-background-color: #ecf0f1;");
        sideMenu.setPrefWidth(250);

        Label contentLabel = new Label("Welcome to CarGo Admin Dashboard");
        contentLabel.setFont(new Font("Arial", 18));
        contentLabel.setStyle("-fx-text-fill: #34495e;");

        VBox contentArea = new VBox(contentLabel);
        contentArea.setSpacing(20);
        contentArea.setPadding(new Insets(30));

        mainLayout = new BorderPane();
        mainLayout.setLeft(sideMenu);
        mainLayout.setCenter(contentArea);
        mainLayout.setTop(topBar);

        bookingsButton.setOnAction(e -> showBookingsContent());
        fleetButton.setOnAction(e -> showFleetContent());
        usersButton.setOnAction(e -> showUsersContent());
        analyticsButton.setOnAction(e -> showAnalyticsContent());

        Scene scene = new Scene(mainLayout, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createNavButton(String text) {
        Button button = new Button(text);
        styleButton(button, "#27ae60", "#ffffff");
        return button;
    }

    private void styleButton(Button button, String backgroundColor, String textColor) {
        button.setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: " + textColor + "; "
                + "-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10px; -fx-border-radius: 8px; "
                + "-fx-pref-width: 200px; -fx-effect: dropshadow(gaussian, #7f8c8d, 5, 0.0, 0, 1);");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #45b39d; -fx-text-fill: #ffffff; "
                + "-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10px; -fx-border-radius: 8px; "
                + "-fx-pref-width: 200px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: "
                + textColor
                + "; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10px; -fx-border-radius: 8px; "
                + "-fx-pref-width: 200px;"));
    }

    private void styleTextField(TextField textField) {
        textField.setStyle("-fx-padding: 10px; -fx-border-radius: 8px; -fx-border-color: #bdc3c7; "
                + "-fx-background-color: #ecf0f1; -fx-font-size: 14px;");
        textField.setPrefWidth(300);
    }

    private void showBookingsContent() {
        Label bookingsLabel = new Label("Bookings Overview");
        bookingsLabel.setFont(new Font("Arial", 24));
        bookingsLabel.setStyle("-fx-text-fill: #27ae60;");

        TextField searchField = new TextField();
        styleTextField(searchField);
        searchField.setPromptText("Search Bookings");

        VBox cardsContainer = new VBox();
        cardsContainer.setSpacing(20);
        cardsContainer.setPadding(new Insets(20));

        String[][] bookingData = {
                { "John Doe", "Toyota Camry", "2024-09-01", "2024-09-07", "Booking #1" },
                { "Jane Smith", "Ford Mustang", "2024-09-05", "2024-09-12", "Booking #2" },
                { "Mark Johnson", "BMW X5", "2024-09-10", "2024-09-15", "Booking #3" },
                { "Lisa White", "Honda Accord", "2024-09-12", "2024-09-18", "Booking #4" },
                { "Mike Brown", "Chevrolet Malibu", "2024-09-15", "2024-09-20", "Booking #5" }
        };

        bookingCards = FXCollections.observableArrayList();

        for (String[] booking : bookingData) {
            HBox card = createBookingCard(booking);
            bookingCards.add(card);
        }

        cardsContainer.getChildren().addAll(bookingCards);

        ScrollPane scrollPane = new ScrollPane(cardsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);

        VBox bookingsContent = new VBox(bookingsLabel, searchField, scrollPane);
        bookingsContent.setSpacing(20);
        bookingsContent.setPadding(new Insets(30));

        searchField.textProperty()
                .addListener((observable, oldValue, newValue) -> filterBookings(newValue, cardsContainer));

        mainLayout.setCenter(bookingsContent);
    }

    // Method to create a booking card
    private HBox createBookingCard(String[] booking) {
        HBox card = new HBox();
        card.setSpacing(10);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-radius: 10px;");

        VBox bookingDetails = new VBox();
        Label userName = new Label("User: " + booking[0]);
        Label carName = new Label("Car: " + booking[1]);
        Label bookingDate = new Label("Booking Date: " + booking[2]);
        Label endDate = new Label("End Date: " + booking[3]);
        Label bookingNumber = new Label(booking[4]);

        userName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
        carName.setStyle("-fx-font-size: 14px; -fx-text-fill: #27ae60;");
        bookingDate.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");
        endDate.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");
        bookingNumber.setStyle("-fx-font-size: 14px; -fx-text-fill: #27ae60;");

        bookingDetails.getChildren().addAll(userName, carName, bookingDate, endDate, bookingNumber);
        bookingDetails.setSpacing(5);

        card.getChildren().addAll(bookingDetails);
        return card;
    }

    private void filterBookings(String searchText, VBox container) {
        container.getChildren().clear();
        for (HBox card : bookingCards) {
            Label userName = (Label) ((VBox) card.getChildren().get(0)).getChildren().get(0);
            Label carName = (Label) ((VBox) card.getChildren().get(0)).getChildren().get(1);
            Label bookingNumber = (Label) ((VBox) card.getChildren().get(0)).getChildren().get(4);

            if (userName.getText().toLowerCase().contains(searchText.toLowerCase()) ||
                    carName.getText().toLowerCase().contains(searchText.toLowerCase()) ||
                    bookingNumber.getText().toLowerCase().contains(searchText.toLowerCase())) {
                container.getChildren().add(card);
            }
        }
    }

    private void showFleetContent() {
        Label fleetLabel = new Label("Fleet Management");
        fleetLabel.setFont(new Font("Arial", 24));
        fleetLabel.setStyle("-fx-text-fill: #27ae60;");

        TextField searchField = new TextField();
        styleTextField(searchField);
        searchField.setPromptText("Search Fleet");

        VBox cardsContainer = new VBox();
        cardsContainer.setSpacing(20);
        cardsContainer.setPadding(new Insets(20));

        String[][] carData = {
                { "Toyota Camry", "$30/day", "/resources/car1.png" },
                { "Ford Mustang", "$40/day", "/resources/car2.png" },
                { "BMW X5", "$50/day", "/resources/car3.png" },
                { "Honda Accord", "$35/day", "/resources/car4.png" },
                { "Chevrolet Malibu", "$33/day", "/resources/car5.png" },
                { "Nissan Altima", "$32/day", "/resources/car6.png" },
                { "Hyundai Sonata", "$28/day", "/resources/car7.png" },
                { "Kia Optima", "$30/day", "/resources/car8.png" },
        };

        ObservableList<HBox> carCards = FXCollections.observableArrayList();

        for (String[] car : carData) {
            HBox card = createCarCard(car);
            carCards.add(card);
        }

        cardsContainer.getChildren().addAll(carCards);

        ScrollPane scrollPane = new ScrollPane(cardsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);

        VBox fleetContent = new VBox(fleetLabel, searchField, scrollPane);
        fleetContent.setSpacing(20);
        fleetContent.setPadding(new Insets(30));

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterCars(newValue, carCards, cardsContainer);
        });

        mainLayout.setCenter(fleetContent);
    }

    private HBox createCarCard(String[] car) {
        HBox card = new HBox();
        card.setSpacing(10);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-radius: 10px;");

        InputStream imageStream = getClass().getResourceAsStream(car[2]);
        if (imageStream == null) {
            System.err.println("Image not found: " + car[2]);
            return card;
        }
        ImageView carImage = new ImageView(new Image(imageStream));
        carImage.setFitWidth(100);
        carImage.setFitHeight(100);
        carImage.setPreserveRatio(true);

        VBox carDetails = new VBox();
        Label carName = new Label(car[0]);
        carName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
        Label carPrice = new Label(car[1]);
        carPrice.setStyle("-fx-font-size: 14px; -fx-text-fill: #27ae60;");

        carDetails.getChildren().addAll(carName, carPrice);
        carDetails.setSpacing(5);

        card.getChildren().addAll(carImage, carDetails);
        return card;
    }

    private void filterCars(String searchText, ObservableList<HBox> carCards, VBox cardsContainer) {
        cardsContainer.getChildren().clear();

        for (HBox card : carCards) {
            Label carName = (Label) ((VBox) card.getChildren().get(1)).getChildren().get(0); // Get the car name label
            Label carPrice = (Label) ((VBox) card.getChildren().get(1)).getChildren().get(1); // Get the car price label

            if (carName.getText().toLowerCase().contains(searchText.toLowerCase()) ||
                    carPrice.getText().toLowerCase().contains(searchText.toLowerCase())) {
                cardsContainer.getChildren().add(card); // Add card if it matches
            }
        }
    }

    private void showUsersContent() {
        Label usersLabel = new Label("User Management");
        usersLabel.setFont(new Font("Arial", 24));
        usersLabel.setStyle("-fx-text-fill: #27ae60;");

        TextField searchField = new TextField();
        styleTextField(searchField);
        searchField.setPromptText("Search Users");

        VBox cardsContainer = new VBox();
        cardsContainer.setSpacing(20);
        cardsContainer.setPadding(new Insets(20));

        String[][] userData = {
                { "John Doe", "john.doe@example.com", "+1234567890", "1990-01-01" },
                { "Jane Smith", "jane.smith@example.com", "+1234567891", "1992-02-02" },
                { "Mark Johnson", "mark.johnson@example.com", "+1234567892", "1988-03-03" },
                { "Lisa White", "lisa.white@example.com", "+1234567893", "1995-04-04" },
                { "Mike Brown", "mike.brown@example.com", "+1234567894", "1987-05-05" }
        };

        ObservableList<HBox> userCards = FXCollections.observableArrayList();

        for (String[] user : userData) {
            HBox card = createUserCard(user);
            userCards.add(card);
        }

        cardsContainer.getChildren().addAll(userCards);

        ScrollPane scrollPane = new ScrollPane(cardsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);

        VBox usersContent = new VBox(usersLabel, searchField, scrollPane);
        usersContent.setSpacing(20);
        usersContent.setPadding(new Insets(30));

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterUsers(newValue, userCards, cardsContainer);
        });

        mainLayout.setCenter(usersContent);
    }

    private HBox createUserCard(String[] user) {
        HBox card = new HBox();
        card.setSpacing(10);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-radius: 10px;");

        VBox userDetails = new VBox();
        Label userName = new Label("Name: " + user[0]);
        Label emailId = new Label("Email: " + user[1]);
        Label contactNumber = new Label("Contact: " + user[2]);
        Label dob = new Label("DOB: " + user[3]);

        userName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
        emailId.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");
        contactNumber.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");
        dob.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        userDetails.getChildren().addAll(userName, emailId, contactNumber, dob);
        userDetails.setSpacing(5);

        card.getChildren().addAll(userDetails);
        return card;
    }

    private void filterUsers(String searchText, ObservableList<HBox> userCards, VBox cardsContainer) {
        cardsContainer.getChildren().clear();

        for (HBox card : userCards) {
            Label userName = (Label) ((VBox) card.getChildren().get(0)).getChildren().get(0); // Get the user name label
            Label emailId = (Label) ((VBox) card.getChildren().get(0)).getChildren().get(1); // Get the email label
            Label contactNumber = (Label) ((VBox) card.getChildren().get(0)).getChildren().get(2); // Get the contact
                                                                                                   // label
            Label dob = (Label) ((VBox) card.getChildren().get(0)).getChildren().get(3); // Get the DOB label

            if (userName.getText().toLowerCase().contains(searchText.toLowerCase()) ||
                    emailId.getText().toLowerCase().contains(searchText.toLowerCase()) ||
                    contactNumber.getText().toLowerCase().contains(searchText.toLowerCase()) ||
                    dob.getText().toLowerCase().contains(searchText.toLowerCase())) {
                cardsContainer.getChildren().add(card); // Add card if it matches
            }
        }
    }

    private void showAnalyticsContent() {
        Label analyticsLabel = new Label("User Analytics");
        analyticsLabel.setFont(new Font("Arial", 24));
        analyticsLabel.setStyle("-fx-text-fill: #27ae60;");

        GridPane analyticsGrid = new GridPane();
        analyticsGrid.setPadding(new Insets(20));
        analyticsGrid.setVgap(15);
        analyticsGrid.setHgap(50);
        analyticsGrid.setStyle(
                "-fx-background-color: #f9f9f9; -fx-border-color: #bdc3c7; -fx-border-radius: 10px; -fx-padding: 20px;");

        Label totalUsersLabel = new Label("Total Users:");
        totalUsersLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");

        Label totalUsersValue = new Label("100");
        totalUsersValue.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");

        Label activeBookingsLabel = new Label("Active Bookings:");
        activeBookingsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");

        Label activeBookingsValue = new Label("35");
        activeBookingsValue.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");

        Label totalRevenueLabel = new Label("Total Revenue:");
        totalRevenueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");

        Label totalRevenueValue = new Label("$25,000");
        totalRevenueValue.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");

        Label carsAvailableLabel = new Label("Cars Available:");
        carsAvailableLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");

        Label carsAvailableValue = new Label("25");
        carsAvailableValue.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");

        analyticsGrid.add(totalUsersLabel, 0, 0);
        analyticsGrid.add(totalUsersValue, 1, 0);
        analyticsGrid.add(activeBookingsLabel, 0, 1);
        analyticsGrid.add(activeBookingsValue, 1, 1);
        analyticsGrid.add(totalRevenueLabel, 0, 2);
        analyticsGrid.add(totalRevenueValue, 1, 2);
        analyticsGrid.add(carsAvailableLabel, 0, 3);
        analyticsGrid.add(carsAvailableValue, 1, 3);

        PieChart pieChart = createPieChart();
        LineChart<String, Number> lineChart = createLineChart();

        VBox chartContainer = new VBox(analyticsLabel, analyticsGrid, pieChart, lineChart);
        chartContainer.setSpacing(20);
        chartContainer.setPadding(new Insets(30));

        ScrollPane scrollPane = new ScrollPane(chartContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #ffffff;");
        scrollPane.setPrefHeight(500);

        mainLayout.setCenter(scrollPane);
    }

    private PieChart createPieChart() {
        PieChart pieChart = new PieChart();
        pieChart.setTitle("User Distribution by Type");

        PieChart.Data slice1 = new PieChart.Data("Regular Users", 60);
        PieChart.Data slice2 = new PieChart.Data("Premium Users", 30);
        PieChart.Data slice3 = new PieChart.Data("Admin Users", 10);

        pieChart.getData().addAll(slice1, slice2, slice3);
        pieChart.setPrefHeight(300);
        pieChart.setLegendVisible(true);
        pieChart.setStyle("-fx-background-color: #ffffff; -fx-border-color: #bdc3c7; -fx-border-radius: 10px;");

        return pieChart;
    }

    private LineChart<String, Number> createLineChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of Users");

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Monthly User Growth");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Users");
        series.getData().add(new XYChart.Data<>("Jan", 20));
        series.getData().add(new XYChart.Data<>("Feb", 50));
        series.getData().add(new XYChart.Data<>("Mar", 80));
        series.getData().add(new XYChart.Data<>("Apr", 70));
        series.getData().add(new XYChart.Data<>("May", 90));
        series.getData().add(new XYChart.Data<>("Jun", 110));

        lineChart.getData().add(series);
        lineChart.setPrefHeight(300);
        lineChart.setStyle("-fx-background-color: #ffffff; -fx-border-color: #bdc3c7; -fx-border-radius: 10px;");

        return lineChart;
    }

    public static void main(String[] args) {
        launch(args);
    }
}