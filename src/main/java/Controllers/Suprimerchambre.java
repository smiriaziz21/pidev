package Controllers;

import Entites.Room;
import Services.ServiceRoom;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class Suprimerchambre {

    @FXML
    private TableView<Room> roomTable;



    @FXML
    private TableColumn<Room, Integer> colHotelId;

    @FXML
    private TableColumn<Room, String> colRoomNumber;

    @FXML
    private TableColumn<Room, Integer> colCapacity;

    @FXML
    private Button deleteButton;

    private ServiceRoom serviceRoom;
    private ObservableList<Room> roomList;

    public Suprimerchambre() {
        serviceRoom = new ServiceRoom();
    }

    @FXML
    public void initialize() {
        setupTable();
        loadRooms();
    }

    private void setupTable() {

        colHotelId.setCellValueFactory(new PropertyValueFactory<>("hotelId"));
        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        roomTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            deleteButton.setDisable(newSelection == null);
        });
    }

    private int responsableId = 1; // Remplace 1 par l'ID du responsable connecté

    private void loadRooms() {
        try {
            roomList = FXCollections.observableArrayList(serviceRoom.getRoomsByResponsableId(responsableId));
            roomTable.setItems(roomList);
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les chambres : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void deleteRoom() {
        Room selectedRoom = roomTable.getSelectionModel().getSelectedItem();
        if (selectedRoom == null) {
            showAlert("Avertissement", "Aucune chambre sélectionnée pour la suppression!", Alert.AlertType.WARNING);
            return;
        }

        try {
            serviceRoom.supprimer(selectedRoom);
            roomList.remove(selectedRoom);
            showAlert("Succès", "Chambre supprimée avec succès!", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de supprimer la chambre : " + e.getMessage(), Alert.AlertType.ERROR);
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
