package Controllers;

import Entite.Activities;
import Service.ServiceActivities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AjouterActiviteController {

    @FXML
    private TextField txtname;
    @FXML
    private TextField txtDescription;
    @FXML
    private DatePicker dpStartDate;
    @FXML
    private DatePicker dpEndDate;
    @FXML
    private TextField txtLocation;


    private final ServiceActivities service = new ServiceActivities();

    private AfficheActiviteController parentController;
    private Activities currentActivity = null;
    private int responsableId;
    private int eventId;

    // Link to parent controller for table refresh
    public void setParentController(AfficheActiviteController controller) {
        this.parentController = controller;
    }

    // Load existing activity for updating
    public void setActivity(Activities activity) {
        if (activity != null) {
            this.currentActivity = activity;
            txtname.setText(activity.getName());
            txtDescription.setText(activity.getDescription());
            dpStartDate.setValue(activity.getStartDate().toLocalDate());
            dpEndDate.setValue(activity.getEndDate().toLocalDate());
            txtLocation.setText(activity.getLocation());

        }
    }



    @FXML
    void delete(ActionEvent event) {
        try {
            if (currentActivity == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "No activity selected for deletion!");
                return;
            }

            service.supprimer(currentActivity);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Activity deleted successfully!");

            if (parentController != null) {
                parentController.loadActivities();
            }

            closeWindow();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error deleting activity: " + e.getMessage());
        }
    }



    private boolean validateInputs() {
        // Check if any required field is empty or null
        if (txtname.getText().isEmpty() ||
                txtDescription.getText().isEmpty() ||
                dpStartDate.getValue() == null ||
                dpEndDate.getValue() == null ||
                txtLocation.getText().isEmpty()
                ) {

            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields.");
            return false;
        }

        // You no longer need to parse the dates because DatePicker returns a LocalDate.
        // If you want to ensure the dates are logically valid (e.g., start date before end date), you can add:
        if (dpStartDate.getValue().isAfter(dpEndDate.getValue())) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Start date must be before end date.");
            return false;
        }

        // Validate that Responsible ID and Event ID are numbers.


        return true;
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) txtname.getScene().getWindow();
        stage.close();
    }
    public void setEventId(int eventId) {
        this.eventId = eventId;

    }
    public void setResponsableId(int responsableId) {
        this.responsableId = responsableId;

    }
    @FXML
    void ajouter(ActionEvent event) {
        try {
            if (!validateInputs()) return;

            LocalDateTime startDateTime = LocalDateTime.of(dpStartDate.getValue(), LocalTime.MIDNIGHT);
            LocalDateTime endDateTime = LocalDateTime.of(dpEndDate.getValue(), LocalTime.MIDNIGHT);

            // Create new activity with the event ID and responsable ID
            Activities activity = new Activities(
                    eventId, // Automatically set event ID
                    txtname.getText(),
                    txtDescription.getText(),
                    startDateTime,
                    endDateTime,
                    txtLocation.getText(),
                    responsableId // Automatically set responsable ID
            );

            service.ajouter(activity);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Activity added successfully!");

            // Refresh table in the main window
            if (parentController != null) {
                parentController.loadActivities();
            }

            closeWindow();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding activity: " + e.getMessage());
        }
    }
}
