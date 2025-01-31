package Controllers;

import Entite.Activities;
import Service.ServiceActivities;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ModifierActiviteController {

    @FXML
    private TextField txtname;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextField txtLocation;
    @FXML
    private TextField txtStartDate;  // Start Date field
    @FXML
    private TextField txtEndDate;    // End Date field

    private Activities currentActivity;
    private final ServiceActivities service = new ServiceActivities();

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void initData(Activities activity) {
        this.currentActivity = activity;

        txtname.setText(activity.getName());
        txtDescription.setText(activity.getDescription());
        txtLocation.setText(activity.getLocation());


        txtStartDate.setText(formatter.format(activity.getStartDate()));
        txtEndDate.setText(formatter.format(activity.getEndDate()));
    }


    @FXML
    private void updateActivity() {
        try {

            currentActivity.setName(txtname.getText());
            currentActivity.setDescription(txtDescription.getText());
            currentActivity.setLocation(txtLocation.getText());


            LocalDateTime startDate = LocalDateTime.parse(txtStartDate.getText(), formatter);
            LocalDateTime endDate = LocalDateTime.parse(txtEndDate.getText(), formatter);

            currentActivity.setStartDate(startDate);
            currentActivity.setEndDate(endDate);


            service.update(currentActivity);


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

