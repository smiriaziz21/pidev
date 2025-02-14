package Controllers;

import Entite.Hotels;
import Entite.Room;
import Service.ServiceHotel;
import Service.ServiceRoom;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.util.List;

public class Ajouterunechambre {

    @FXML private ComboBox<Hotels> cbHotel;  // Remplace tfHotelId par ComboBox
    @FXML private TextField tfRoomNumber;
    @FXML private TextField tfCapacity;

    private final ServiceRoom serviceRoom = new ServiceRoom();
    private final ServiceHotel serviceHotel = new ServiceHotel();
    private  int currentResponsableId ;
    // ID statique pour le moment
    public void setResponsableId(int responsableId) {
        this.currentResponsableId = responsableId;
        System.out.println(currentResponsableId);
        loadHotels();
    }
    @FXML
    private void initialize() {
           // Charger les hôtels
    }

    void loadHotels() {
        try {
            System.out.println(currentResponsableId);
            List<Hotels> hotels = serviceHotel.getAllByResponsableId(currentResponsableId);
            System.out.println(currentResponsableId);
            if (hotels.isEmpty()) {
                showAlert("Information", "Aucun hôtel trouvé", "Aucun hôtel n'est associé à ce responsable.", Alert.AlertType.INFORMATION);
                cbHotel.getItems().clear();
            } else {
                cbHotel.setItems(FXCollections.observableArrayList(hotels));
                cbHotel.setConverter(new StringConverter<Hotels>() {
                    @Override
                    public String toString(Hotels hotel) {
                        return hotel.getName(); // Affiche seulement le nom dans le ComboBox
                    }

                    @Override
                    public Hotels fromString(String string) {
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
            Hotels selectedHotel = cbHotel.getValue();
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