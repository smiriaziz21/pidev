package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

public class StripePaymentController {

    @FXML
    private TextField txtCardNumber;
    @FXML
    private TextField txtExpirationDate;
    @FXML
    private TextField txtCVV;

    private static final String STRIPE_SECRET_KEY = "sk_test_51QrmDoQFI5b4XOKUfRopAVApUMuuPoQTJbYeFXMXq41KWzD1mSAXn00MvyIF1qe1iG0ughizNTeCAqWUrrvs4AuW0042Fsh9pX";

    @FXML
    public void processPayment() {
        String cardNumber = txtCardNumber.getText().trim();
        String expirationDate = txtExpirationDate.getText().trim();
        String cvv = txtCVV.getText().trim();

        if (!validateCardDetails(cardNumber, expirationDate, cvv)) {
            return;
        }

        try {
            Stripe.apiKey = STRIPE_SECRET_KEY;

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(1000L)
                    .setCurrency("usd")
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);
            showAlert("Succès", "Paiement effectué avec succès! ID: " + paymentIntent.getId());

            Stage stage = (Stage) txtCardNumber.getScene().getWindow();
            stage.close();

        } catch (StripeException e) {
            showAlert("Erreur", "Erreur lors du paiement: " + e.getMessage());
        }
    }

    private boolean validateCardDetails(String cardNumber, String expirationDate, String cvv) {
        if (cardNumber.isEmpty() || expirationDate.isEmpty() || cvv.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return false;
        }

        if (!cardNumber.matches("\\d{16}")) {
            showAlert("Erreur", "Numéro de carte invalide. Il doit contenir 16 chiffres.");
            return false;
        }

        if (!expirationDate.matches("(0[1-9]|1[0-2])/(\\d{2})")) {
            showAlert("Erreur", "Format de la date d'expiration invalide. Utilisez MM/YY.");
            return false;
        }

        if (!cvv.matches("\\d{3}")) {
            showAlert("Erreur", "Le CVV doit contenir 3 chiffres.");
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
