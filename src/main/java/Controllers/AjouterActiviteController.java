package Controllers;

import Entite.Activities;
import Service.ServiceActivities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AjouterActiviteController {

    @FXML
    private TextField txtname;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextField txtStartDate;
    @FXML
    private TextField txtEndDate;
    @FXML
    private TextField txtLocation;
    @FXML
    private TextField txtResponsibleId;
    @FXML
    private TextField txtIdEvent;

    private final ServiceActivities service = new ServiceActivities();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Method for adding an activity
    @FXML
    void ajouter(ActionEvent event) {
        try {
            // Retrieving all the input values
            String name = txtname.getText();
            String description = txtDescription.getText();
            LocalDateTime startDate = LocalDateTime.parse(txtStartDate.getText(), formatter);
            LocalDateTime endDate = LocalDateTime.parse(txtEndDate.getText(), formatter);
            String location = txtLocation.getText();
            int responsibleId = Integer.parseInt(txtResponsibleId.getText());
            int idEvent = Integer.parseInt(txtIdEvent.getText());

            // Creating a new activity
            Activities activity = new Activities(idEvent, name, description, startDate, endDate, location, responsibleId);

            // Adding the activity
            service.ajouter(activity);
            System.out.println("Activity added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding activity: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    @FXML
    void update(ActionEvent event) {
        try {
            String name = txtname.getText();
            String description = txtDescription.getText();
            LocalDateTime startDate = LocalDateTime.parse(txtStartDate.getText(), formatter);
            LocalDateTime endDate = LocalDateTime.parse(txtEndDate.getText(), formatter);
            String location = txtLocation.getText();
            int responsibleId = Integer.parseInt(txtResponsibleId.getText());
            int idEvent = Integer.parseInt(txtIdEvent.getText());

            // TODO: Replace with actual ID from the selected activity
            int activityIdToUpdate = 1;

            Activities activity = new Activities(activityIdToUpdate, idEvent, name, description, startDate, endDate, location, responsibleId);

            service.update(activity);
            System.out.println("Activity updated successfully!");

        } catch (SQLException e) {
            System.out.println("Error updating activity: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    @FXML
    void delete(ActionEvent event) {
        try {
            // TODO: Replace with actual ID from the selected activity
            int activityIdToDelete = 1; // This should be dynamically retrieved

            // Create a dummy Activities object with only the ID set
            Activities activityToDelete = new Activities();
            activityToDelete.setId(activityIdToDelete);

            service.supprimer(activityToDelete); // Pass an Activities object
            System.out.println("Activity deleted successfully!");

        } catch (SQLException e) {
            System.out.println("Error deleting activity: " + e.getMessage());
        }
    }



    @FXML
    void afficher(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheActivities.fxml"));
            Parent root = loader.load();
            txtname.getScene().setRoot(root); // Fixed from txtage to txtname
        } catch (IOException e) {
            System.out.println("Error loading display page: " + e.getMessage());
        }
    }
}
