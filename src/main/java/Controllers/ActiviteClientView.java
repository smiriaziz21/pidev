package Controllers;

import Entite.Activities;
import Service.ServiceActivities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ActiviteClientView implements Initializable {
    @FXML
    private TableView<Activities> tablev;
    @FXML
    private TableColumn<Activities, String> colname;
    @FXML
    private TableColumn<Activities, String> coldescription;
    @FXML
    private TableColumn<Activities, String> collocation;
    @FXML
    private TableColumn<Activities, String> colstartDate;
    @FXML
    private TableColumn<Activities, String> colendDate;
    @FXML
    private TableColumn<Activities, Void> colActions; // Corrected to Activities and Void

    @FXML
    private Label lblWeather;


    private final ServiceActivities service = new ServiceActivities();
    private ObservableList<Activities> activitiesList = FXCollections.observableArrayList();
    private int eventId;
    public void setEventId(int eventId) {
        this.eventId = eventId;
        loadActivities(); // Reload activities for the specific event
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadActivities();

        colname.setCellValueFactory(new PropertyValueFactory<>("name"));
        coldescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        collocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colstartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colendDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));

    }


    void loadActivities() {
        try {
            // Fetch activities for the specific event
            List<Activities> allActivities = service.getAll();
            List<Activities> filteredActivities = allActivities.stream()
                    .filter(activity -> activity.getIdEvent() == eventId)
                    .collect(Collectors.toList());

            // Update the table
            activitiesList.setAll(filteredActivities);
            tablev.setItems(activitiesList);
        } catch (SQLException e) {
            showErrorMessage("Error loading activities", e);
        }
    }









    @FXML
    void Back(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
        Parent root = loader.load();
        tablev.getScene().setRoot(root);
    }

    private void showErrorMessage(String message, Exception e) {

        Alert alert = new Alert(Alert.AlertType.ERROR, message + ": " + e.getMessage(), ButtonType.OK);
        alert.showAndWait();
        e.printStackTrace();
    }
}
