package Controllers;

import Entite.Activities;
import Service.ServiceActivities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.time.LocalDateTime;
import java.sql.SQLException;

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

    ServiceActivities service = new ServiceActivities();

    // Method for adding an activity
    @FXML
    void ajouter(ActionEvent event) {
        // Retrieving all the input values
        String name = txtname.getText();
        String description = txtDescription.getText();
        LocalDateTime startDate = LocalDateTime.parse(txtStartDate.getText()); 
        LocalDateTime endDate = LocalDateTime.parse(txtEndDate.getText());
        String location = txtLocation.getText();
        Integer responsibleId = Integer.parseInt(txtResponsibleId.getText());
        int idEvent = Integer.parseInt(txtIdEvent.getText());

        // Creating a new activity with all fields
        Activities activity = new Activities(idEvent, name, description, startDate, endDate, location, responsibleId);

        try {
            service.ajouter(activity);
            System.out.println("Activity added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding activity: " + e.getMessage());
        }
    }

    @FXML
    void update(ActionEvent event) {
        String name = txtname.getText();
        String description = txtDescription.getText();
        LocalDateTime startDate = LocalDateTime.parse(txtStartDate.getText());
        LocalDateTime endDate = LocalDateTime.parse(txtEndDate.getText());
        String location = txtLocation.getText();
        Integer responsibleId = Integer.parseInt(txtResponsibleId.getText());
        int idEvent = Integer.parseInt(txtIdEvent.getText());

        int activityIdToUpdate = 1;


        Activities activity = new Activities(activityIdToUpdate, idEvent, name, description, startDate, endDate, location, responsibleId);

        try {
            service.update(activity);
            System.out.println("Activity updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating activity: " + e.getMessage());
        }
    }


    @FXML
    void delete(ActionEvent event) {

        int activityIdToDelete = 1;

        try {
            service.supprimer(new Activities(activityIdToDelete, 0, null, null, null, null, null, null));
            System.out.println("Activity deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting activity: " + e.getMessage());
        }
    }
}
