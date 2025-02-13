package Controllers;

import Entite.Facture;
import Service.ServiceFacture;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class FactureModifierController {

    @FXML
    private TextField txtReservationId;
    @FXML
    private TextField txtAmount;
    @FXML
    private DatePicker dpDate;
    @FXML
    private ComboBox<String> comboModePaiement;
    @FXML
    private ComboBox<String> comboConditionPaiement;
    @FXML
    private Button btnPayment;

    private Facture currentFacture;
    private final ServiceFacture service = new ServiceFacture();

    private final List<String> modesPaiement = Arrays.asList("Espèces", "Carte Bancaire", "Virement");
    private final List<String> conditionsPaiement = Arrays.asList("À la réservation", "À l’arrivée", "50% à la réservation, 50% à l’événement");

    public void initData(Facture facture) {
        this.currentFacture = facture;

        txtReservationId.setText(String.valueOf(facture.getReservationId()));
        txtReservationId.setDisable(true);
        txtAmount.setText(String.valueOf(facture.getAmount()));

        comboModePaiement.getItems().setAll(modesPaiement);
        comboConditionPaiement.getItems().setAll(conditionsPaiement);

        comboModePaiement.setValue(facture.getModeDePaiement() != null ? facture.getModeDePaiement() : modesPaiement.get(0));
        comboConditionPaiement.setValue(facture.getConditionDePaiement() != null ? facture.getConditionDePaiement() : conditionsPaiement.get(0));

        if (facture.getDate() != null) {
            dpDate.setValue(facture.getDate());
        }

        dpDate.getEditor().setDisable(true);
        dpDate.getEditor().setOpacity(1);
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

        if (dpDate.getValue().isBefore(LocalDate.now())) {
            showAlert("Erreur", "La date ne peut pas être dans le passé.");
            return;
        }

        if (comboModePaiement.getValue() == null) {
            showAlert("Erreur", "Le mode de paiement ne peut pas être vide.");
            return;
        }

        if (comboConditionPaiement.getValue() == null) {
            showAlert("Erreur", "La condition de paiement ne peut pas être vide.");
            return;
        }

        try {
            currentFacture.setAmount(amount);
            currentFacture.setDate(dpDate.getValue());
            currentFacture.setModeDePaiement(comboModePaiement.getValue());
            currentFacture.setConditionDePaiement(comboConditionPaiement.getValue());

            service.update(currentFacture);
            showAlert("Succès", "Facture mise à jour avec succès.");
            Stage stage = (Stage) txtReservationId.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la mise à jour de la facture: " + e.getMessage());
        }
    }

    @FXML

    private void handlePayment() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PaymentDialog.fxml"));
            AnchorPane dialogPane = loader.load();
            Stage paymentDialogStage = new Stage();
            paymentDialogStage.setTitle("Paiement");
            paymentDialogStage.initModality(Modality.APPLICATION_MODAL);
            paymentDialogStage.setScene(new Scene(dialogPane));

            paymentDialogStage.showAndWait();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du dialog de paiement: " + e.getMessage());
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
    public void goBack() {
        Stage stage = (Stage) txtReservationId.getScene().getWindow();
        stage.close();
    }
}
