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
import java.util.ResourceBundle;

public class AfficheActiviteController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadActivities();
        setupActionButtons();

    }



    void loadActivities() {
        try {
            activitiesList.setAll(service.getAll());
            tablev.setItems(activitiesList);


            colname.setCellValueFactory(new PropertyValueFactory<>("name"));
            coldescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            collocation.setCellValueFactory(new PropertyValueFactory<>("location"));
            colstartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            colendDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        } catch (SQLException e) {
            showErrorMessage("Error loading activities", e);
        }
    }

    private void setupActionButtons() {

        Callback<TableColumn<Activities, Void>, TableCell<Activities, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnUpdate = new Button("Update");
            private final Button btnDelete = new Button("Delete");

            {
                btnUpdate.setOnAction((ActionEvent event) -> {
                    Activities activity = getTableView().getItems().get(getIndex());
                    openUpdateWindow(activity);
                });
                btnDelete.setOnAction((ActionEvent event) -> {
                    Activities activity = getTableView().getItems().get(getIndex());
                    deleteActivity(activity);
                });

                // Style buttons
                btnUpdate.setStyle("-fx-background-color: #ffa726; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
                btnDelete.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10, btnUpdate, btnDelete);
                    setGraphic(hbox);
                }
            }
        };
        colActions.setCellFactory(cellFactory);
    }

    private void deleteActivity(Activities activity) {
        try {
            service.delete(activity.getId());
            activitiesList.remove(activity);
        } catch (SQLException e) {
            showErrorMessage("Error deleting activity", e);
        }
    }

    private void openUpdateWindow(Activities activity) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierActivite.fxml"));
            Parent root = loader.load();

            ModifierActiviteController controller = loader.getController();
            controller.initData(activity);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update Activity");
            stage.showAndWait();

            loadActivities();
        } catch (IOException e) {
            showErrorMessage("Error opening update window", e);
        }
    }

    private void openAddWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouteActivities.fxml"));
            Parent root = loader.load();
            AjouterActiviteController controller = loader.getController();
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Activity");
            stage.showAndWait();

            loadActivities();
        } catch (IOException e) {
            showErrorMessage("Error opening add window", e);
        }
    }

    @FXML
    private void handleAddActivity(ActionEvent event) {
        openAddWindow();
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
