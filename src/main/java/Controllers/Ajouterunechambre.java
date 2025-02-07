package Controllers;

import Entites.Hotel;
import Entites.Room;
import Services.ServiceHotel;
import Services.ServiceRoom;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.util.List;

public class Ajouterunechambre {

    @FXML private ComboBox<Hotel> cbHotel;  // Remplace tfHotelId par ComboBox
    @FXML private TextField tfRoomNumber;
    @FXML private TextField tfCapacity;

    private final ServiceRoom serviceRoom = new ServiceRoom();
    private final ServiceHotel serviceHotel = new ServiceHotel();
    private final int currentResponsableId = 1;  // ID statique pour le moment

    @FXML
    private void initialize() {
        loadHotels(); // Charger les hôtels
    }

    private void loadHotels() {
        try {
            List<Hotel> hotels = serviceHotel.getAllByResponsableId(currentResponsableId);

            if (hotels.isEmpty()) {
                showAlert("Information", "Aucun hôtel trouvé", "Aucun hôtel n'est associé à ce responsable.", Alert.AlertType.INFORMATION);
                cbHotel.getItems().clear();
            } else {
                cbHotel.setItems(FXCollections.observableArrayList(hotels));
                cbHotel.setConverter(new StringConverter<Hotel>() {
                    @Override
                    public String toString(Hotel hotel) {
                        return hotel.getName(); // Affiche seulement le nom dans le ComboBox
                    }

                    @Override
                    public Hotel fromString(String string) {
                        return null; // Pas utilisé, mais requis par l'interface
                    }
                });
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les hôtels", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void handleAddRoom() {
        try {
            Hotel selectedHotel = cbHotel.getValue();
            if (selectedHotel == null) {
                showAlert("Erreur", "Sélection d'hôtel requise", "Veuillez choisir un hôtel.", Alert.AlertType.WARNING);
                return;
            }

            String roomNumber = tfRoomNumber.getText().trim();
            if (roomNumber.isEmpty()) {
                showAlert("Erreur", "Numéro de chambre requis", "Veuillez entrer un numéro de chambre.", Alert.AlertType.WARNING);
                return;
            }

            int capacity;
            try {
                capacity = Integer.parseInt(tfCapacity.getText().trim());
                if (capacity <= 0) {
                    showAlert("Erreur", "Capacité invalide", "Veuillez entrer un nombre valide supérieur à 0.", Alert.AlertType.WARNING);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Capacité invalide", "Veuillez entrer un nombre valide.", Alert.AlertType.WARNING);
                return;
            }

            Room room = new Room(selectedHotel.getId(), roomNumber, capacity);

            serviceRoom.ajouterPSTM(room);
            showAlert("Succès", null, "Chambre ajoutée avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        } catch (SQLException e) {
            showAlert("Erreur SQL", "Échec de l'ajout", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clearFields() {
        cbHotel.getSelectionModel().clearSelection();
        tfRoomNumber.clear();
        tfCapacity.clear();
    }

    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
