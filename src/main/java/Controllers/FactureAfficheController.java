package Controllers;

import Entite.Facture;
import Service.ServiceFacture;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FactureAfficheController implements Initializable {

    @FXML
    private TableView<Facture> tablev;

    @FXML
    private TableColumn<Facture, Integer> colId;

    @FXML
    private TableColumn<Facture, Integer> colReservationId;

    @FXML
    private TableColumn<Facture, Double> colAmount;

    @FXML
    private TableColumn<Facture, String> colDate;

    @FXML
    private TableColumn<Facture, Void> colActions;

    private final ServiceFacture service = new ServiceFacture();
    private ObservableList<Facture> facturesList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadFactures();
        setupActionButtons();
    }

    private void loadFactures() {
        try {
            facturesList.setAll(service.getAll());
            tablev.setItems(facturesList);

            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colReservationId.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
            colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupActionButtons() {

        Callback<TableColumn<Facture, Void>, TableCell<Facture, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnUpdate = new Button("Update");
            private final Button btnDelete = new Button("Delete");

            {
                btnUpdate.setOnAction((ActionEvent event) -> {
                    Facture facture = getTableView().getItems().get(getIndex());
                    openUpdateWindow(facture);
                });
                btnDelete.setOnAction((ActionEvent event) -> {
                    Facture facture = getTableView().getItems().get(getIndex());
                    deleteFacture(facture);
                });

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

    private void deleteFacture(Facture facture) {
        try {
            service.supprimer(facture);
            facturesList.remove(facture);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openUpdateWindow(Facture facture) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FactureModifier.fxml"));
            Parent root = loader.load();

            FactureModifierController controller = loader.getController();
            controller.initData(facture);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update Facture");
            stage.showAndWait();

            loadFactures();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Back(ActionEvent event) {
        System.out.println("Back to previous screen");
    }
}
