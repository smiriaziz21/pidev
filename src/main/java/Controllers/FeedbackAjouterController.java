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
    private ComboBox<String> comboClientId;
    @FXML
    private ComboBox<String> comboEventId;
    @FXML
    private ComboBox<Integer> comboRating;
    @FXML
    private DatePicker datePicker;

    private final ServiceFeedback service = new ServiceFeedback();

    public void initialize() {
        try {
            List<String> clientIds = service.getClientIdsAsStrings();
            if (clientIds != null && !clientIds.isEmpty()) {
                comboClientId.getItems().clear();
                comboClientId.getItems().addAll(clientIds);
            } else {
                showAlert("Erreur", "Aucun ID de client disponible.");
            }

            List<String> eventIds = service.getEventIdsAsStrings();
            if (eventIds != null && !eventIds.isEmpty()) {
                comboEventId.getItems().clear();
                comboEventId.getItems().addAll(eventIds);
            } else {
                showAlert("Erreur", "Aucun ID d'événement disponible.");
            }

            comboRating.getItems().clear();
            comboRating.getItems().addAll(1, 2, 3, 4, 5);

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
        if (comboClientId.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner un ID de client.");
            return;
        }

        if (comboEventId.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner un ID d'événement.");
            return;
        }

        if (txtComment.getText().isEmpty()) {
            showAlert("Erreur", "Le commentaire ne peut pas être vide.");
            return;
        }

        if (comboRating.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner une note.");
            return;
        }

        int rating = comboRating.getValue();

        if (datePicker.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner une date.");
            return;
        }

        Feedback feedback = new Feedback(
                Integer.parseInt(comboClientId.getValue()),
                Integer.parseInt(comboEventId.getValue()),
                txtComment.getText(),
                rating,
                datePicker.getValue()
        );

        try {
            service.ajouter(feedback);
            System.out.println("Feedback ajouté avec succès!");

            ((Stage) txtComment.getScene().getWindow()).close();

        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout du feedback: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void retour(ActionEvent event) {
        ((Stage) txtComment.getScene().getWindow()).close();
    }
}
