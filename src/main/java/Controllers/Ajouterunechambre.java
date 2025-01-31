package Controllers;

import Entites.Room;
import Services.ServiceRoom;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class Ajouterunechambre {

    @FXML private TextField tfHotelId;
    @FXML private TextField tfRoomNumber;
    @FXML private TextField tfCapacity;

    private final ServiceRoom serviceRoom = new ServiceRoom();

    @FXML
    private void handleAddRoom() {
        try {
            Room room = new Room(
                    Integer.parseInt(tfHotelId.getText()),
                    tfRoomNumber.getText(),
                    Integer.parseInt(tfCapacity.getText())
            );

            serviceRoom.ajouterPSTM(room);
            showAlert("Succès", "Chambre ajoutée avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Données invalides", "Veuillez vérifier les valeurs saisies", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Erreur SQL", "Échec de l'ajout", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clearFields() {
        tfHotelId.clear();
        tfRoomNumber.clear();
        tfCapacity.clear();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}