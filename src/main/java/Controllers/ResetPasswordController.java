package Controllers;

import Services.DaoLogin;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class ResetPasswordController {
    @FXML
    private TextField email;
    @FXML
    private TextField generatedPasswordField;
    @FXML
    private Button copyButton;

    @FXML
    private void resetPassword() {
        String userEmail = email.getText().trim();

        if (userEmail.isEmpty()) {
            showAlert("Error", "Please enter your email address.", Alert.AlertType.ERROR);
            return;
        }

        String newPassword = DaoLogin.resetPasswordWithRandom(userEmail);

        if (newPassword != null) {
            generatedPasswordField.setText(newPassword);
            showAlert("Success", "Your new password has been generated and sent to your email.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Failed to reset password. Please try again.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void copyToClipboard() {
        String password = generatedPasswordField.getText();
        if (!password.isEmpty()) {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(password);
            clipboard.setContent(content);
            showAlert("Copied", "Password copied to clipboard!", Alert.AlertType.INFORMATION);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
