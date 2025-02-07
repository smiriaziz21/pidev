package Controllers;

import Entite.Facture;
import Service.ServiceFacture;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class FactureModifierController {

    @FXML
    private TextField txtReservationId;
    @FXML
    private TextField txtAmount;
    @FXML
    private DatePicker dpDate;

    private Facture currentFacture;
    private final ServiceFacture service = new ServiceFacture();

    public void initData(Facture facture) {
        this.currentFacture = facture;
        txtReservationId.setText(String.valueOf(facture.getReservationId()));
        txtReservationId.setDisable(true);

        txtAmount.setText(String.valueOf(facture.getAmount()));

        if (facture.getDate() != null) {
            dpDate.setValue(facture.getDate());
        } else {
            dpDate.setValue(null);
        }
    }

    @FXML
    private void updateFacture() {
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

        if (dpDate.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner une date.");
            return;
        }

        try {
            currentFacture.setAmount(amount);
            currentFacture.setDate(dpDate.getValue());
            service.update(currentFacture);
            Stage stage = (Stage) txtReservationId.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la mise à jour de la facture: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur inattendue: " + e.getMessage());
        }
    }

    @FXML
    private void generateFacture() {
        try {
            // Assuming service.savePdf() generates and saves the PDF
            service.savePdf(currentFacture);
            showAlert("Succès", "Le PDF a été généré avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la génération du PDF: " + e.getMessage());
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
        Stage stage = (Stage) txtReservationId.getScene().getWindow();
        stage.close();
    }
}
