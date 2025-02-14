package Controllers;

import Entite.Activities;
import Service.ServiceActivities;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ModifierActiviteController {

    @FXML
    private TextField txtname;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextField txtLocation;

    // Replace these TextFields with DatePicker controls
    @FXML
    private DatePicker dpStartDate;  // DatePicker for Start Date
    @FXML
    private DatePicker dpEndDate;    // DatePicker for End Date

    private Activities currentActivity;
    private final ServiceActivities service = new ServiceActivities();

    public void initData(Activities activity) {
        this.currentActivity = activity;

        txtname.setText(activity.getName());
        txtDescription.setText(activity.getDescription());
        txtLocation.setText(activity.getLocation());

        // Set the DatePicker values using the date portion of the LocalDateTime
        LocalDate startDate = activity.getStartDate().toLocalDate();
        LocalDate endDate = activity.getEndDate().toLocalDate();
        dpStartDate.setValue(startDate);
        dpEndDate.setValue(endDate);
    }

    @FXML
    private void updateActivity() {
        try {
            currentActivity.setName(txtname.getText());
            currentActivity.setDescription(txtDescription.getText());
            currentActivity.setLocation(txtLocation.getText());

            // Create LocalDateTime objects from the DatePicker values (defaulting time to MIDNIGHT)
            LocalDateTime startDateTime = LocalDateTime.of(dpStartDate.getValue(), LocalTime.MIDNIGHT);
            LocalDateTime endDateTime = LocalDateTime.of(dpEndDate.getValue(), LocalTime.MIDNIGHT);

            currentActivity.setStartDate(startDateTime);
            currentActivity.setEndDate(endDateTime);

            service.update(currentActivity);

            // Close the current window after update
            Stage stage = (Stage) txtname.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goBack(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage) txtname.getScene().getWindow();
        stage.close();
    }
}
