
package Application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Payment extends Application {

        @Override
        public void start(Stage primaryStage) {
                primaryStage.setTitle("Modern Payment Gateway");

                Label titleLabel = new Label("Payment Gateway");
                titleLabel.setStyle(
                                "-fx-font-family: 'Arial'; -fx-font-size: 24px; -fx-text-fill: #2d3436; -fx-font-weight: bold;");

                Label nameLabel = new Label("Name on Card:");
                nameLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-text-fill: #333;");

                TextField nameField = new TextField();
                nameField.setPromptText("Enter your name");
                nameField.setStyle(
                                "-fx-font-size: 14px; -fx-padding: 10px; -fx-border-radius: 5px; -fx-background-radius: 5px; "
                                                + "-fx-border-color: #ccc; -fx-border-width: 1px; -fx-background-color: #fff;"
                                                + "-fx-pref-width: 300px;");

                Label cardNumberLabel = new Label("Card Number:");
                cardNumberLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-text-fill: #333;");

                TextField cardNumberField = new TextField();
                cardNumberField.setPromptText("xxxx-xxxx-xxxx-xxxx");
                cardNumberField.setStyle(
                                "-fx-font-size: 14px; -fx-padding: 10px; -fx-border-radius: 5px; -fx-background-radius: 5px; "
                                                + "-fx-border-color: #ccc; -fx-border-width: 1px; -fx-background-color: #fff;"
                                                + "-fx-pref-width: 300px;");

                Label expiryDateLabel = new Label("Expiry Date:");
                expiryDateLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-text-fill: #333;");

                DatePicker expiryDatePicker = new DatePicker();
                expiryDatePicker.setPromptText("MM/YY");
                expiryDatePicker.setStyle(
                                "-fx-font-size: 14px; -fx-padding: 10px; -fx-border-radius: 5px; -fx-background-radius: 5px; "
                                                + "-fx-border-color: #ccc; -fx-border-width: 1px; -fx-background-color: #fff;"
                                                + "-fx-pref-width: 300px;");
                expiryDatePicker.setConverter(new StringConverter<LocalDate>() {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");

                        @Override
                        public String toString(LocalDate date) {
                                return (date != null) ? formatter.format(date) : "";
                        }

                        @Override
                        public LocalDate fromString(String string) {
                                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, formatter)
                                                : null;
                        }
                });

                Label cvvLabel = new Label("CVV:");
                cvvLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-text-fill: #333;");

                PasswordField cvvField = new PasswordField();
                cvvField.setPromptText("* * *");
                cvvField.setStyle(
                                "-fx-font-size: 14px; -fx-padding: 10px; -fx-border-radius: 5px; -fx-background-radius: 5px; "
                                                + "-fx-border-color: #ccc; -fx-border-width: 1px; -fx-background-color: #fff;"
                                                + "-fx-pref-width: 300px;");

                Button payButton = new Button("Pay Now");
                payButton.setStyle(
                                "-fx-background-color: #0984e3; -fx-text-fill: white; -fx-font-size: 16px; "
                                                + "-fx-font-weight: bold; -fx-padding: 10px 30px; -fx-background-radius: 30px;"
                                                + "-fx-border-radius: 30px; -fx-cursor: hand;");

                payButton.setOnMouseEntered(e -> payButton.setStyle(
                                "-fx-background-color: #74b9ff; -fx-text-fill: white; -fx-font-size: 16px; "
                                                + "-fx-font-weight: bold; -fx-padding: 10px 30px; -fx-background-radius: 30px;"
                                                + "-fx-border-radius: 30px; -fx-cursor: hand;"));
                payButton.setOnMouseExited(e -> payButton.setStyle(
                                "-fx-background-color: #0984e3; -fx-text-fill: white; -fx-font-size: 16px; "
                                                + "-fx-font-weight: bold; -fx-padding: 10px 30px; -fx-background-radius: 30px;"
                                                + "-fx-border-radius: 30px; -fx-cursor: hand;"));

                payButton.setOnAction(e -> {
                        String name = nameField.getText();
                        String cardNumber = cardNumberField.getText();
                        LocalDate expiryDate = expiryDatePicker.getValue();
                        String cvv = cvvField.getText();
                        if (name.isEmpty() || cardNumber.isEmpty() || expiryDate == null || cvv.isEmpty()) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setContentText("Please fill in all fields.");
                                alert.show();
                                return;
                        } else {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Payment Confirmation");
                                alert.setHeaderText(null);
                                alert.setContentText("Payment Successful!");

                                alert.showAndWait();

                                primaryStage.close();
                        }
                });

                VBox layout = new VBox(15);
                layout.setPadding(new Insets(30, 50, 30, 50));
                layout.setAlignment(Pos.CENTER);
                layout.getChildren().addAll(titleLabel, nameLabel, nameField, cardNumberLabel, cardNumberField,
                                expiryDateLabel, expiryDatePicker, cvvLabel, cvvField, payButton);

                layout.setStyle("-fx-background-color: #f5f6fa;");

                Scene scene = new Scene(layout, 400, 450);
                primaryStage.setScene(scene);
                primaryStage.show();
        }

        public static void main(String[] args) {
                launch(args);
        }
}
