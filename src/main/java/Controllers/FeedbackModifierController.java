package Controllers;

import Entite.Feedback;
import Service.ServiceFeedback;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class FeedbackModifierController {

    @FXML
    private TextField txtClientId;
    @FXML
    private TextField txtEventId;
    @FXML
    private TextArea txtComment;
    @FXML
    private ComboBox<Integer> comboRating; // Use ComboBox with Integer type
    @FXML
    private DatePicker dpDate;

    private Feedback currentFeedback;
    private final ServiceFeedback service = new ServiceFeedback();

    public void initData(Feedback feedback) {
        this.currentFeedback = feedback;
        txtClientId.setText(String.valueOf(feedback.getClientId()));
        txtClientId.setDisable(true);

        txtEventId.setText(String.valueOf(feedback.getEventId()));
        txtEventId.setDisable(true);

        txtComment.setText(feedback.getComment());
        comboRating.setValue(feedback.getRating()); // Set the selected rating

        if (feedback.getDate() != null) {
            dpDate.setValue(feedback.getDate());
        } else {
            dpDate.setValue(null);
        }
    }

    @FXML
    private void updateFeedback() {
        if (txtComment.getText().isEmpty()) {
            showAlert("Erreur", "Le commentaire ne peut pas être vide.");
            return;
        }

        if (comboRating.getValue() == null) {
            showAlert("Erreur", "La note ne peut pas être vide.");
            return;
        }

        int rating = comboRating.getValue(); // Get the rating directly from ComboBox

        if (rating < 1 || rating > 5) {
            showAlert("Erreur", "La note doit être comprise entre 1 et 5.");
            return;
        }

        if (dpDate.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner une date.");
            return;
        }

        try {
            currentFeedback.setComment(txtComment.getText());
            currentFeedback.setRating(rating);
            currentFeedback.setDate(dpDate.getValue());
            service.update(currentFeedback);
            Stage stage = (Stage) txtClientId.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la mise à jour du feedback: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur inattendue: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goBack(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage) txtClientId.getScene().getWindow();
        stage.close();
    }
}
