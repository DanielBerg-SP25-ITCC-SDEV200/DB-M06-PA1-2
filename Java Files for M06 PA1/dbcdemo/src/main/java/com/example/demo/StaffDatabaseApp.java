package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;

public class StaffDatabaseApp extends Application {

    private TextField idField = new TextField();
    private TextField lastNameField = new TextField();
    private TextField firstNameField = new TextField();
    private TextField miField = new TextField();
    private TextField addressField = new TextField();
    private TextField cityField = new TextField();
    private TextField stateField = new TextField();
    private TextField telephoneField = new TextField();
    private TextField emailField = new TextField();
    private TextArea biographyField = new TextArea();  // New biography field

    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        // Labels
        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("First Name:"), 0, 2);
        grid.add(firstNameField, 1, 2);
        grid.add(new Label("MI:"), 0, 3);
        grid.add(miField, 1, 3);
        grid.add(new Label("Address:"), 0, 4);
        grid.add(addressField, 1, 4);
        grid.add(new Label("City:"), 0, 5);
        grid.add(cityField, 1, 5);
        grid.add(new Label("State:"), 0, 6);
        grid.add(stateField, 1, 6);
        grid.add(new Label("Telephone:"), 0, 7);
        grid.add(telephoneField, 1, 7);
        grid.add(new Label("Email:"), 0, 8);
        grid.add(emailField, 1, 8);
        grid.add(new Label("Biography:"), 0, 9);  // New label for biography
        grid.add(biographyField, 1, 9);  // New text area for biography

        // Buttons
        Button viewButton = new Button("View");
        Button insertButton = new Button("Insert");
        Button updateButton = new Button("Update");
        Button clearButton = new Button("Clear");  // Clear button

        grid.add(viewButton, 0, 10);
        grid.add(insertButton, 1, 10);
        grid.add(updateButton, 2, 10);
        grid.add(clearButton, 0, 11, 3, 1);  // Clear button span across all columns

        // Button actions
        viewButton.setOnAction(e -> viewRecord());
        insertButton.setOnAction(e -> insertRecord());
        updateButton.setOnAction(e -> updateRecord());
        clearButton.setOnAction(e -> clearFields());  // Clear fields action

        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setTitle("Staff Database");
        primaryStage.setScene(scene);
        primaryStage.show();

        connectToDatabase();
    }

    private void connectToDatabase() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            String url = "jdbc:mariadb://localhost:3306/Staff"; // Replace with your database URL
            String username = "root"; // Replace with your database username
            String password = ""; // Replace with your database password

            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not connect to the database.");
        }
    }

    private void viewRecord() {
        String id = idField.getText();
        if (id.isEmpty()) {
            showAlert("Input Error", "Please enter an ID.");
            return;
        }

        try {
            String sql = "SELECT * FROM Staff WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                lastNameField.setText(resultSet.getString("lastName"));
                firstNameField.setText(resultSet.getString("firstName"));
                miField.setText(resultSet.getString("mi"));
                addressField.setText(resultSet.getString("address"));
                cityField.setText(resultSet.getString("city"));
                stateField.setText(resultSet.getString("state"));
                telephoneField.setText(resultSet.getString("telephone"));
                emailField.setText(resultSet.getString("email"));
                biographyField.setText(resultSet.getString("biography"));  // Set biography field
            } else {
                showAlert("Record Not Found", "Staff record with ID " + id + " not found.");
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error viewing record.");
        }
    }

    private void insertRecord() {
        try {
            String sql = "INSERT INTO Staff (id, lastName, firstName, mi, address, city, state, telephone, email, biography) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, idField.getText());
            statement.setString(2, lastNameField.getText());
            statement.setString(3, firstNameField.getText());
            statement.setString(4, miField.getText());
            statement.setString(5, addressField.getText());
            statement.setString(6, cityField.getText());
            statement.setString(7, stateField.getText());
            statement.setString(8, telephoneField.getText());
            statement.setString(9, emailField.getText());
            statement.setString(10, biographyField.getText());  // Insert biography field

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                showAlert("Success", "Record inserted successfully.");
                clearFields();
            } else {
                showAlert("Error", "Failed to insert record.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error inserting record.");
        }
    }

    private void updateRecord() {
        try {
            String sql = "UPDATE Staff SET lastName = ?, firstName = ?, mi = ?, address = ?, city = ?, state = ?, telephone = ?, email = ?, biography = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, lastNameField.getText());
            statement.setString(2, firstNameField.getText());
            statement.setString(3, miField.getText());
            statement.setString(4, addressField.getText());
            statement.setString(5, cityField.getText());
            statement.setString(6, stateField.getText());
            statement.setString(7, telephoneField.getText());
            statement.setString(8, emailField.getText());
            statement.setString(9, biographyField.getText());  // Update biography field
            statement.setString(10, idField.getText());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert("Success", "Record updated successfully.");
                clearFields();
            } else {
                showAlert("Record Not Found", "Staff record with ID " + idField.getText() + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error updating record.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        idField.clear();
        lastNameField.clear();
        firstNameField.clear();
        miField.clear();
        addressField.clear();
        cityField.clear();
        stateField.clear();
        telephoneField.clear();
        emailField.clear();
        biographyField.clear();  // Clear biography field
    }

    public static void main(String[] args) {
        launch(args);
    }
}

