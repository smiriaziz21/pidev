package Controllers;

import Entite.Activities;
import Service.ServiceActivities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    private AfficheActiviteController parentController;
    private Activities currentActivity = null;

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
            txtStartDate.setText(activity.getStartDate().format(formatter));
            txtEndDate.setText(activity.getEndDate().format(formatter));
            txtLocation.setText(activity.getLocation());
            txtResponsibleId.setText(String.valueOf(activity.getResponsibleId()));
            txtIdEvent.setText(String.valueOf(activity.getIdEvent()));
        }
    }

    @FXML
    void ajouter(ActionEvent event) {
        try {
            if (!validateInputs()) return;

            // Create new activity
            Activities activity = new Activities(
                    Integer.parseInt(txtIdEvent.getText()),
                    txtname.getText(),
                    txtDescription.getText(),
                    LocalDateTime.parse(txtStartDate.getText(), formatter),
                    LocalDateTime.parse(txtEndDate.getText(), formatter),
                    txtLocation.getText(),
                    Integer.parseInt(txtResponsibleId.getText())
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

    @FXML
    void update(ActionEvent event) {
        try {
            if (currentActivity == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "No activity selected for update!");
                return;
            }
            if (!validateInputs()) return;

            // Update the existing activity
            Activities updatedActivity = new Activities(
                    currentActivity.getId(), // Use existing ID
                    Integer.parseInt(txtIdEvent.getText()),
                    txtname.getText(),
                    txtDescription.getText(),
                    LocalDateTime.parse(txtStartDate.getText(), formatter),
                    LocalDateTime.parse(txtEndDate.getText(), formatter),
                    txtLocation.getText(),
                    Integer.parseInt(txtResponsibleId.getText())
            );

            service.update(updatedActivity);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Activity updated successfully!");

            if (parentController != null) {
                parentController.loadActivities();
            }

            closeWindow();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating activity: " + e.getMessage());
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

    @FXML
    void afficher(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheActivities.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Activities List");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Error loading display page: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        if (txtname.getText().isEmpty() || txtDescription.getText().isEmpty() ||
                txtStartDate.getText().isEmpty() || txtEndDate.getText().isEmpty() ||
                txtLocation.getText().isEmpty() || txtResponsibleId.getText().isEmpty() ||
                txtIdEvent.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields.");
            return false;
        }

        try {
            LocalDateTime.parse(txtStartDate.getText(), formatter);
            LocalDateTime.parse(txtEndDate.getText(), formatter);
        } catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid date format. Use 'yyyy-MM-dd HH:mm:ss'.");
            return false;
        }

        try {
            Integer.parseInt(txtResponsibleId.getText());
            Integer.parseInt(txtIdEvent.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Responsible ID and Event ID must be numbers.");
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) txtname.getScene().getWindow();
        stage.close();
    }
}
