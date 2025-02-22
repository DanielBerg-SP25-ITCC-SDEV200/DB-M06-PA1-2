package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Random;

public class DBConnectionPanel extends Application {

    private TextField urlField, userField, passwordField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        urlField = new TextField("jdbc:mariadb://localhost:3306/mydb");  // Example URL
        userField = new TextField("root");  // Example username
        passwordField = new PasswordField();

        Button connectButton = new Button("Connect to Database");
        connectButton.setOnAction(event -> connectToDatabase());

        VBox vbox = new VBox(10, new Label("Database URL:"), urlField,
                new Label("Username:"), userField,
                new Label("Password:"), passwordField,
                connectButton);
        Scene scene = new Scene(vbox, 300, 250);
        primaryStage.setTitle("Database Connection");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void connectToDatabase() {
        String dbUrl = urlField.getText();
        String dbUsername = userField.getText();
        String dbPassword = passwordField.getText();

        try {
            // Register the MariaDB JDBC driver
            Class.forName("org.mariadb.jdbc.Driver");

            // Attempt to connect to the database
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

            // Display connection success
            showInfoDialog("Connection Success", "Connected to the database successfully!");

            // Perform batch update and non-batch update
            performBatchUpdate(conn);
            performNonBatchUpdate(conn);

            // Close the connection
            conn.close();
        } catch (ClassNotFoundException e) {
            showErrorDialog("Driver Error", "MariaDB JDBC driver not found: " + e.getMessage());
        } catch (SQLException e) {
            showErrorDialog("Connection Error", "Failed to connect to the database: " + e.getMessage());
        }
    }

    private void performBatchUpdate(Connection conn) {
        long startTime = System.nanoTime();  // Record the start time

        try (Statement stmt = conn.createStatement()) {
            for (int i = 0; i < 1000; i++) {
                double num1 = Math.random();
                double num2 = Math.random();
                double num3 = Math.random();
                stmt.addBatch("INSERT INTO Temp (num1, num2, num3) VALUES (" + num1 + ", " + num2 + ", " + num3 + ")");
            }

            stmt.executeBatch();  // Execute the batch update
        } catch (SQLException e) {
            showErrorDialog("Batch Update Error", "Error during batch update: " + e.getMessage());
        }

        long endTime = System.nanoTime();  // Record the end time
        long elapsedTime = endTime - startTime;  // Calculate elapsed time

        showInfoDialog("Batch Update Completed", "Batch update completed. Elapsed time: " + elapsedTime / 1_000_000 + " ms");
    }

    private void performNonBatchUpdate(Connection conn) {
        long startTime = System.nanoTime();  // Record the start time

        try (Statement stmt = conn.createStatement()) {
            for (int i = 0; i < 1000; i++) {
                double num1 = Math.random();
                double num2 = Math.random();
                double num3 = Math.random();
                String sql = "INSERT INTO Temp (num1, num2, num3) VALUES (" + num1 + ", " + num2 + ", " + num3 + ")";
                stmt.executeUpdate(sql);  // Execute the update one by one
            }
        } catch (SQLException e) {
            showErrorDialog("Non-Batch Update Error", "Error during non-batch update: " + e.getMessage());
        }

        long endTime = System.nanoTime();  // Record the end time
        long elapsedTime = endTime - startTime;  // Calculate elapsed time

        showInfoDialog("Non-Batch Update Completed", "Non-batch update completed. Elapsed time: " + elapsedTime / 1_000_000 + " ms");
    }

    private void showInfoDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



