package Controllers;

import Entite.Facture;
import Service.ServiceFacture;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

public class FactureAjouterController {

    @FXML
    private TextField txtAmount;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Integer> comboReservationId;
    @FXML
    private ComboBox<String> comboConditionDePaiement;
    @FXML
    private ComboBox<String> comboModeDePaiement;

    private final ServiceFacture service = new ServiceFacture();

    public void initialize() {
        try {
            List<Integer> reservationIds = service.getReservationIds();
            comboReservationId.getItems().addAll(reservationIds);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des IDs de réservation: " + e.getMessage());
        }

        comboModeDePaiement.getItems().addAll("Carte Bancaire", "Virement", "Chèque");

        comboConditionDePaiement.getItems().addAll(
                "À la réservation",
                "À l’arrivée",
                "50% à la réservation, 50% à l’événement"
        );
    }

    @FXML
    void ajouter() {
        if (comboReservationId.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner un ID de réservation.");
            return;
        }

        if (txtAmount.getText().isEmpty()) {
            showAlert("Erreur", "Le montant ne peut pas être vide.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(txtAmount.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le montant doit être un nombre valide.");
            return;
        }

        if (datePicker.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner une date.");
            return;
        }

        if (comboConditionDePaiement.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner une condition de paiement.");
            return;
        }

        if (comboModeDePaiement.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner un mode de paiement.");
            return;
        }

        Date date = Date.valueOf(datePicker.getValue());

        Facture facture = new Facture(comboReservationId.getValue().intValue(), amount, date.toLocalDate(),
                comboConditionDePaiement.getValue(), comboModeDePaiement.getValue());

        try {
            service.ajouter(facture);
            System.out.println("Facture ajoutée avec succès!");
            ((Stage) txtAmount.getScene().getWindow()).close();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout de la facture: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void retour(ActionEvent event) {
        ((Stage) txtAmount.getScene().getWindow()).close();
    }
}
