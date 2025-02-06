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
    private DatePicker datePicker;  // DatePicker for selecting date
    @FXML
    private ComboBox<Integer> comboReservationId;  // ComboBox for selecting reservation ID

    private final ServiceFacture service = new ServiceFacture();

    // Initialize the controller
    public void initialize() {
        try {
            // Fetch reservation IDs and populate the ComboBox without showing alerts
            List<Integer> reservationIds = service.getReservationIds();
            comboReservationId.getItems().addAll(reservationIds);
        } catch (SQLException e) {
            System.out.println("Error fetching reservation IDs: " + e.getMessage());
        }
    }

    @FXML
    void ajouter() {
        // Only show alerts when clicking the "Ajouter" button

        // Check if reservation ID is selected
        if (comboReservationId.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner un ID de réservation.");
            return;  // This will stop further execution if the condition fails
        }

        // Check if the amount field is empty
        if (txtAmount.getText().isEmpty()) {
            showAlert("Erreur", "Le montant ne peut pas être vide.");
            return;
        }

        // Parse the amount entered and check for valid number format
        double amount;
        try {
            amount = Double.parseDouble(txtAmount.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le montant doit être un nombre valide.");
            return;
        }

        // Check if the date is selected
        if (datePicker.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner une date.");
            return;
        }

        // All fields are valid, proceed with facture creation
        Date date = Date.valueOf(datePicker.getValue());

        // Correcting the constructor call with intValue() for reservationId
        Facture facture = new Facture(comboReservationId.getValue().intValue(), amount, date.toLocalDate());

        try {
            // Add the facture
            service.ajouter(facture);
            System.out.println("Facture ajoutée avec succès!");

            // Optionally close the window after adding
            ((Stage) txtAmount.getScene().getWindow()).close();

        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout de la facture: " + e.getMessage());
        }
    }

    // Helper method to show French alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void retour(ActionEvent event) {
        // Close the current window and go back to the previous screen
        ((Stage) txtAmount.getScene().getWindow()).close();
    }
}
