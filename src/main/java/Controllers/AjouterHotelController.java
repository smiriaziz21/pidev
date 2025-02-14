package Controllers;

import Entite.Hotels;
import Service.ServiceHotel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;

public class AjouterHotelController {

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtLocalisation;

    @FXML
    private ComboBox<String> comboEtoiles; // Nouveau champ pour les étoiles

    @FXML
    private Button btnAjouter;

    private int currentResponsableId;

    public void setResponsableId(int responsableId) {
        this.currentResponsableId = responsableId;
    }

    @FXML
    void initialize() {
        // Ajout des valeurs dans le ComboBox au lancement du formulaire
        comboEtoiles.getItems().addAll("1 ★", "2 ★★", "3 ★★★", "4 ★★★★", "5 ★★★★★");
        comboEtoiles.setValue("3 ★★★"); // Valeur par défaut
    }

    @FXML
    void ajouterHotel(ActionEvent event) {
        // Vérification des champs
        if (txtNom.getText().isEmpty() || txtLocalisation.getText().isEmpty() || comboEtoiles.getValue() == null) {
            showAlert(Alert.AlertType.ERROR,
                    "Champs manquants",
                    "Veuillez remplir tous les champs requis",
                    "Tous les champs doivent être remplis pour ajouter un hôtel.");
            return;
        }

        try {
            String nom = txtNom.getText();
            String localisation = txtLocalisation.getText();
            int idResponsable = currentResponsableId; // À remplacer avec l'ID réel
            int etoiles = Character.getNumericValue(comboEtoiles.getValue().charAt(0)); // Extraction du chiffre
            System.out.println(idResponsable);
            Hotels hotel = new Hotels(nom, localisation, idResponsable, etoiles);

            ServiceHotel serviceHotel = new ServiceHotel();
            serviceHotel.ajouter(hotel);

            showAlert(Alert.AlertType.INFORMATION,
                    "Succès",
                    "Hôtel ajouté avec succès",
                    "L'hôtel a été enregistré ");

            txtNom.clear();
            txtLocalisation.clear();
            comboEtoiles.setValue("3 ★★★");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR,
                    "Erreur technique",
                    "Échec de l'ajout",
                    "Erreur lors de la communication avec la base de données :\n" + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}