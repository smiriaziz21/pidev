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
    private DatePicker dateDebut;  // Start Date Picker
    @FXML
    private DatePicker dateFin;    // End Date Picker

    private final ServiceFacture service = new ServiceFacture();
    private ObservableList<Facture> facturesList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadFactures();
        setupActionButtons();

        btnAjouter.setOnMouseEntered(event -> btnAjouter.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white;"));
        btnAjouter.setOnMouseExited(event -> btnAjouter.setStyle("-fx-background-color: #43a047; -fx-text-fill: white;"));

        // Add listeners to filters
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterFactures());
        dateDebut.valueProperty().addListener((observable, oldValue, newValue) -> filterFactures());  // Add listener for start date
        dateFin.valueProperty().addListener((observable, oldValue, newValue) -> filterFactures());    // Add listener for end date
    }

    // Method to load all factures from the service
    private void loadFactures() {
        try {
            facturesList.setAll(service.getAll());
            tablev.setItems(facturesList);

            colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
            colReservationId.setCellValueFactory(cellData -> cellData.getValue().reservationIdProperty().asObject());
            colAmount.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());

            // Format date as string
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

    // Method to filter factures based on search and date range
    private void filterFactures() {
        String keyword = searchField.getText().toLowerCase();
        LocalDate startDate = dateDebut.getValue();
        LocalDate endDate = dateFin.getValue();

        ObservableList<Facture> filteredList = facturesList.stream()
                .filter(facture -> {
                    // Check keyword in various properties
                    boolean matchesKeyword = keyword.isEmpty() ||
                            String.valueOf(facture.getId()).contains(keyword) ||
                            String.valueOf(facture.getReservationId()).contains(keyword) ||
                            String.valueOf(facture.getAmount()).contains(keyword) ||
                            (facture.getDate() != null && facture.getDate().toString().contains(keyword));

                    // Check if date is within the selected range
                    boolean matchesDate = (startDate == null || facture.getDate() != null && !facture.getDate().isBefore(startDate)) &&
                            (endDate == null || facture.getDate() != null && !facture.getDate().isAfter(endDate));

                    return matchesKeyword && matchesDate;
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        tablev.setItems(filteredList);
    }

    // Method to setup actions for Update/Delete buttons
    private void setupActionButtons() {
        Callback<TableColumn<Facture, Void>, TableCell<Facture, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnUpdate = new Button("Update");
            private final Button btnDelete = new Button("Delete");

            {
                // Update button action
                btnUpdate.setOnAction(event -> {
                    Facture facture = getTableView().getItems().get(getIndex());
                    openUpdateWindow(facture);
                });
                // Delete button action
                btnDelete.setOnAction(event -> {
                    Facture facture = getTableView().getItems().get(getIndex());
                    deleteFacture(facture);
                });

                // Button styles
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

    // Method to handle facture deletion
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
                    filterFactures(); // Update the list after deletion
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Method to open the update window for a selected facture
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
            filterFactures(); // Ensure the list is updated after modification
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to open the add window for a new facture
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
            filterFactures(); // Update the list after adding
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Back button action
    @FXML
    private void Back(ActionEvent event) {
        System.out.println("Back to previous screen");
    }
}
