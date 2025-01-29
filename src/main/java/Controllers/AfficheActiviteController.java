package Controllers;

import Entite.Activities;
import Service.ServiceActivities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficheActiviteController {

    @FXML
    private TableColumn<Activities, Integer> colid;

    @FXML
    private TableColumn<Activities, String> colname;

    @FXML
    private TableColumn<Activities, Void> colActions; // New Column for Actions

    @FXML
    private TableView<Activities> tablev;

    private ServiceActivities serviceActivities = new ServiceActivities();

    @FXML
    void initialize() {
        try {
            List<Activities> list = serviceActivities.getAll();
            ObservableList<Activities> ob = FXCollections.observableList(list);
            tablev.setItems(ob);

            colid.setCellValueFactory(new PropertyValueFactory<>("id"));
            colname.setCellValueFactory(new PropertyValueFactory<>("name"));

            // Add action buttons (Edit & Delete)
            addButtonToTable();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void addButtonToTable() {
        Callback<TableColumn<Activities, Void>, TableCell<Activities, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Activities, Void> call(final TableColumn<Activities, Void> param) {
                return new TableCell<>() {
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");

                    {
                        editButton.setStyle("-fx-background-color: #ffa726; -fx-text-fill: white; -fx-border-radius: 5px;");
                        deleteButton.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-border-radius: 5px;");

                        editButton.setOnAction(event -> {
                            Activities activite = getTableView().getItems().get(getIndex());
                            System.out.println("Editing: " + activite.getName());
                            // Implement edit logic here (e.g., open edit form)
                        });

                        deleteButton.setOnAction(event -> {
                            Activities activite = getTableView().getItems().get(getIndex());
                            tablev.getItems().remove(activite);
                            System.out.println("Deleted: " + activite.getName());
                            // Implement delete logic in database if needed
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            editButton.setMinWidth(60);
                            deleteButton.setMinWidth(70);
                            setGraphic(new javafx.scene.layout.HBox(10, editButton, deleteButton));
                        }
                    }
                };
            }
        };

        colActions.setCellFactory(cellFactory);
    }

    @FXML
    void Back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterActivities.fxml"));
        Parent root = loader.load();
        tablev.getScene().setRoot(root);
    }
}
