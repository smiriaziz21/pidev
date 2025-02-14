package Controllers;

import Entite.Hotels;
import Service.ServiceHotel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class SuprimerHotelController {

    @FXML
    private TableView<Hotels> hotelTable;

    @FXML
    private TableColumn<Hotels, Integer> colId;

    @FXML
    private TableColumn<Hotels, String> colName;

    @FXML
    private TableColumn<Hotels, String> colLocation;

    @FXML
    private Button deleteButton;

    @FXML
    private Button goToRoomButton;

    private ServiceHotel serviceHotel;
    private ObservableList<Hotels> hotelList;
    private int currentResponsableId;

    public SuprimerHotelController() {
        serviceHotel = new ServiceHotel();
    }

    @FXML
    public void initialize() {

        setupTable();

    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

        hotelTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            deleteButton.setDisable(newSelection == null);
        });
    }

    public void setResponsableId(int responsableId) {
        this.currentResponsableId = responsableId;
        System.out.println(currentResponsableId);
        loadHotels();
    }

    void loadHotels() {
        try {
            System.out.println(currentResponsableId);
            hotelList = FXCollections.observableArrayList(serviceHotel.getAllByResponsableId(currentResponsableId));
            hotelTable.setItems(hotelList);
        } catch (SQLException e) {
            showAlert("Error", "Could not load hotels: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void deleteHotel() {
        Hotels selectedHotel = hotelTable.getSelectionModel().getSelectedItem();
        if (selectedHotel == null) {
            showAlert("Warning", "No hotel selected for deletion!", Alert.AlertType.WARNING);
            return;
        }

        try {
            serviceHotel.supprimer(selectedHotel);
            hotelList.remove(selectedHotel);
            showAlert("Success", "Hotel deleted successfully!", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Error", "Could not delete hotel: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void goToSupprimerRoom() {
        Hotels selectedHotel = hotelTable.getSelectionModel().getSelectedItem();
        if (selectedHotel == null) {
            showAlert("Warning", "No hotel selected!", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SupprimerRoom.fxml"));
            Parent root = loader.load();

            Suprimerchambre chambreController = loader.getController();
            chambreController.setHotelId(selectedHotel.getId());
            chambreController.setResponsableId(currentResponsableId);

            Stage stage = new Stage();
            stage.setTitle("Supprimer Chambre");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load SuprimerRoom.fxml: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}