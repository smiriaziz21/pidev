package Controllers;

import Entites.Hotel;
import Services.ServiceHotel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class AjouterHotelController {

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtLocalisation;

    @FXML
    private Button btnAjouter;

    @FXML
    void ajouterHotel(ActionEvent event) {
        // Vérification des champs
        if (txtNom.getText().isEmpty() || txtLocalisation.getText().isEmpty()) {
            showAlert(AlertType.ERROR,
                    "Champs manquants",
                    "Veuillez remplir tous les champs requis",
                    "Tous les champs doivent être remplis pour ajouter un hôtel");
            return;
        }

        try {

            String nom = txtNom.getText();
            String localisation = txtLocalisation.getText();
            int idResponsable = 1;

            Hotel hotel = new Hotel(nom, localisation, idResponsable);


            ServiceHotel serviceHotel = new ServiceHotel();
            serviceHotel.ajouter(hotel);

            showAlert(AlertType.INFORMATION,
                    "Succès",
                    "Hôtel ajouté avec succès",
                    "L'hôtel a été enregistré dans la base de données");


            txtNom.clear();
            txtLocalisation.clear();

        } catch (SQLException e) {
            showAlert(AlertType.ERROR,
                    "Erreur technique",
                    "Échec de l'ajout",
                    "Erreur lors de la communication avec la base de données :\n" + e.getMessage());
        }
    }

    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}