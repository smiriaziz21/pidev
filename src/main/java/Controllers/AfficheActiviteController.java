package Controllers;

import Entite.Activities;
import Service.IService;
import Service.ServiceActivities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AfficheActiviteController implements Initializable {

    @FXML
    private TableView<Activities> tablev;
    @FXML
    private TableColumn<Activities, Integer> colid;
    @FXML
    private TableColumn<Activities, String> colname;
    @FXML
    private TableColumn<Activities, Void> colActions;
    @FXML
    private TableColumn<Activities, Void> colDelete;

    @FXML
    private void Back(ActionEvent event) {
        // Code pour revenir à l'écran précédent
        System.out.println("Retour à l'écran précédent");
    }

    private final ServiceActivities service = new ServiceActivities(); // ✅ Déclaration correcte

    private ObservableList<Activities> activitiesList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadActivities();
        setupUpdateButton();
        setupDeleteButton();
    }

    private void loadActivities() {
        try {
            activitiesList.setAll(service.getAll());
            tablev.setItems(activitiesList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void setupDeleteButton() {
        Callback<TableColumn<Activities, Void>, TableCell<Activities, Void>> cellFactory = param ->
                new TableCell<>() {
                    private final Button btnDelete = new Button("Delete");

                    {
                        btnDelete.setOnAction((ActionEvent event) -> {
                            Activities activity = getTableView().getItems().get(getIndex());
                            deleteActivity(activity);
                        });
                        btnDelete.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnDelete);
                        }
                    }
                };

        colDelete.setCellFactory(cellFactory);
    }
    private void deleteActivity(Activities activity) {
        try {
            service.delete(activity.getId());  // Suppression de la base de données
            activitiesList.remove(activity);   // Mise à jour du tableau
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void setupUpdateButton() {
        Callback<TableColumn<Activities, Void>, TableCell<Activities, Void>> cellFactory = param ->
                new TableCell<>() {
                    private final Button btnUpdate = new Button("Update");

                    {
                        btnUpdate.setOnAction((ActionEvent event) -> {
                            Activities activity = getTableView().getItems().get(getIndex());
                            openUpdateWindow(activity);
                        });
                        btnUpdate.setStyle("-fx-background-color: #ffa726; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnUpdate);
                        }
                    }
                };

        colActions.setCellFactory(cellFactory);
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

            loadActivities(); // Refresh after update
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
