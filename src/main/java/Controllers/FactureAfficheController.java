package Controllers;

import Entite.Facture;
import Service.ServiceFacture;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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

    @FXML
    private Button btnAjouter;

    @FXML
    private TextField searchField;

    @FXML
    private DatePicker dateDebut;
    @FXML
    private DatePicker dateFin;
    @FXML
    private TableColumn<Facture, String> colConditionPaiement;

    @FXML
    private TableColumn<Facture, String> colModePaiement;


    private final ServiceFacture service = new ServiceFacture();
    private ObservableList<Facture> facturesList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadFactures();
        setupActionButtons();

        btnAjouter.setOnMouseEntered(event -> btnAjouter.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white;"));
        btnAjouter.setOnMouseExited(event -> btnAjouter.setStyle("-fx-background-color: #43a047; -fx-text-fill: white;"));

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterFactures());
        dateDebut.valueProperty().addListener((observable, oldValue, newValue) -> filterFactures());
        dateFin.valueProperty().addListener((observable, oldValue, newValue) -> filterFactures());
    }

    private void loadFactures() {
        try {
            facturesList.setAll(service.getAll());
            tablev.setItems(facturesList);

            colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
            colReservationId.setCellValueFactory(cellData -> cellData.getValue().reservationIdProperty().asObject());
            colAmount.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
            colConditionPaiement.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getConditionDePaiement()));
            colModePaiement.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getModeDePaiement()));

            colDate.setCellValueFactory(cellData -> {
                if (cellData.getValue().getDate() != null) {
                    return new SimpleStringProperty(cellData.getValue().getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } else {
                    return new SimpleStringProperty("");
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filterFactures() {
        String keyword = searchField.getText().toLowerCase();
        LocalDate startDate = dateDebut.getValue();
        LocalDate endDate = dateFin.getValue();

        ObservableList<Facture> filteredList = facturesList.stream()
                .filter(facture -> {
                    boolean matchesKeyword = keyword.isEmpty() ||
                            String.valueOf(facture.getId()).contains(keyword) ||
                            String.valueOf(facture.getReservationId()).contains(keyword) ||
                            String.valueOf(facture.getAmount()).contains(keyword) ||
                            (facture.getDate() != null && facture.getDate().toString().contains(keyword));

                    boolean matchesDate = (startDate == null || facture.getDate() != null && !facture.getDate().isBefore(startDate)) &&
                            (endDate == null || facture.getDate() != null && !facture.getDate().isAfter(endDate));

                    return matchesKeyword && matchesDate;
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        tablev.setItems(filteredList);
    }

    private void setupActionButtons() {
        Callback<TableColumn<Facture, Void>, TableCell<Facture, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnUpdate = new Button("Update");
            private final Button btnDelete = new Button("Delete");

            {
                btnUpdate.setOnAction(event -> {
                    Facture facture = getTableView().getItems().get(getIndex());
                    openUpdateWindow(facture);
                });
                btnDelete.setOnAction(event -> {
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to delete this facture?");
        alert.setContentText("This action cannot be undone!");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    service.supprimer(facture);
                    facturesList.remove(facture);
                    filterFactures();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
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
            filterFactures();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openAddWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FactureAjouter.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Facture");
            stage.showAndWait();

            loadFactures();
            filterFactures();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Back(ActionEvent event) {
        System.out.println("Back to previous screen");
    }
}
