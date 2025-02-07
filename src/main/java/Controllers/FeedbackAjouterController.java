package Controllers;

import Entite.Feedback;
import Service.ServiceFeedback;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class FeedbackAjouterController {

    @FXML
    private TextArea txtComment;
    @FXML
    private ComboBox<String> comboClientId;  // ComboBox<String> for Client ID
    @FXML
    private ComboBox<String> comboEventId;  // ComboBox<String> for Event ID
    @FXML
    private ComboBox<Integer> comboRating;  // ComboBox<Integer> for Rating
    @FXML
    private DatePicker datePicker;

    private final ServiceFeedback service = new ServiceFeedback();

    // Initialize the controller
    public void initialize() {
        try {
            // Fetch client IDs and populate the ComboBox with String values
            List<String> clientIds = service.getClientIdsAsStrings();  // Method to return List<String>
            if (clientIds != null && !clientIds.isEmpty()) {
                comboClientId.getItems().clear();
                comboClientId.getItems().addAll(clientIds);
            } else {
                showAlert("Erreur", "Aucun ID de client disponible.");
            }

            // Fetch event IDs and populate the ComboBox
            List<String> eventIds = service.getEventIdsAsStrings();  // Method to return List<String>
            if (eventIds != null && !eventIds.isEmpty()) {
                comboEventId.getItems().clear();
                comboEventId.getItems().addAll(eventIds);
            } else {
                showAlert("Erreur", "Aucun ID d'événement disponible.");
            }

            // Populate ratings ComboBox with predefined values
            comboRating.getItems().clear();
            comboRating.getItems().addAll(1, 2, 3, 4, 5);

            // Ensure the DatePicker is initialized
            if (datePicker == null) {
                System.out.println("DatePicker is not initialized!");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching IDs: " + e.getMessage());
            showAlert("Erreur", "Erreur lors de la récupération des IDs.");
        }
    }

    @FXML
    void ajouter() {
        // Check if client ID is selected
        if (comboClientId.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner un ID de client.");
            return;
        }

        // Check if event ID is selected
        if (comboEventId.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner un ID d'événement.");
            return;
        }

        // Check if the comment field is empty
        if (txtComment.getText().isEmpty()) {
            showAlert("Erreur", "Le commentaire ne peut pas être vide.");
            return;
        }

        // Check if the rating field is empty and validate the rating
        if (comboRating.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner une note.");
            return;
        }

        int rating = comboRating.getValue();

        // Check if the date is selected
        if (datePicker.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner une date.");
            return;
        }

        // All fields are valid, proceed with feedback creation
        Feedback feedback = new Feedback(
                Integer.parseInt(comboClientId.getValue()),  // Convert String to Integer
                Integer.parseInt(comboEventId.getValue()),  // Convert String to Integer
                txtComment.getText(),
                rating,
                datePicker.getValue()
        );

        try {
            // Add the feedback
            service.ajouter(feedback);
            System.out.println("Feedback ajouté avec succès!");

            // Optionally close the window after adding
            ((Stage) txtComment.getScene().getWindow()).close();

        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout du feedback: " + e.getMessage());
        }
    }

    // Helper method to show French alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void retour(ActionEvent event) {
        // Close the current window and go back to the previous screen
        ((Stage) txtComment.getScene().getWindow()).close();
    }
}
