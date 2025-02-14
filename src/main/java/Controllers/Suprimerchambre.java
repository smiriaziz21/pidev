package Controllers;

import Entite.Room;
import Service.ServiceRoom;
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

    private int responsableId ; // Remplacez avec l'ID du responsable connecté

    public Suprimerchambre() {
        serviceRoom = new ServiceRoom();
    }

    @FXML
    public void initialize() {
        setupTable();
    }

    private void setupTable() {

        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        roomTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            deleteButton.setDisable(newSelection == null);
        });
    }

    // Méthode pour recevoir l'ID de l'hôtel et charger les chambres
    public void setHotelId(int hotelId) {
        System.out.println("ID de l'hôtel reçu : " + hotelId);
        loadRooms(hotelId);
    }
    public void setResponsableId(int responsableId) {
        this.responsableId = responsableId;
    }

    private void loadRooms(int hotelId) {
        try {
            System.out.println("Chargement des chambres pour l'hôtel ID: " + hotelId);
            roomList = FXCollections.observableArrayList(serviceRoom.getRoomsByResponsableId(responsableId, hotelId));
            roomTable.setItems(roomList);
            System.out.println("Nombre de chambres trouvées : " + roomList.size());
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