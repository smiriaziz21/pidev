package Controllers;

import Entite.Activities;
import Service.ServiceActivities;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;

public class ModifierActiviteController {

    @FXML
    private TextField txtname;
    @FXML
    private TextField txtDescription;

    private Activities currentActivity;
    private final ServiceActivities service = new ServiceActivities();

    public void initData(Activities activity) {
        this.currentActivity = activity;
        txtname.setText(activity.getName());
        txtDescription.setText(activity.getDescription());
    }

    @FXML
    private void updateActivity() {
        try {
            currentActivity.setName(txtname.getText());
            currentActivity.setDescription(txtDescription.getText());

            service.update(currentActivity);

            // Close the window after updating
            Stage stage = (Stage) txtname.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
