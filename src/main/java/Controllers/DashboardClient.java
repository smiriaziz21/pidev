package Controllers;

import Utils.DataSource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardClient {
    // Déclaration des composants FXML
    @FXML
    private Button btnEventMan;
    @FXML private Button btnRes;


    @FXML private AnchorPane mainContent;
    @FXML private Button logoutButton; // Add this to your controller

    private int clientId;

    public void setClient(int clientId) {
        this.clientId = clientId;
    }

    @FXML
    public void initialize() {
        configureButtonActions();
    }

    private void configureButtonActions() {
        btnEventMan.setOnAction(e -> loadDataPage("ClientBooking.fxml"));
        btnRes.setOnAction(e -> loadDataPage("FeedbackAffiche.fxml"));


    }



    private void loadDataPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlFile));
            Parent root = loader.load();

            if (loader.getController() instanceof ClientBookingController) {
                ((ClientBookingController) loader.getController())
                        .setClientId(clientId);
            } else if (loader.getController() instanceof FeedbackAfficheController) {
                ((FeedbackAfficheController) loader.getController())
                        .setClientId(clientId);

            }

            mainContent.getChildren().setAll(root);

        } catch (IOException e) {
            showErrorAlert("Erreur de chargement",
                    "Échec du chargement de " + fxmlFile);
        }
    }

    private void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setCurrentResponsableId(int id) {
        this.clientId = id;
    }
    @FXML
    private void handleLogout() {
        try {
            // Load login page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml")); // Ensure correct path
            Parent root = loader.load();

            // Get the current stage and update it with the login screen
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            showAlert("Error Logout failed: " + e.getMessage());
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
}
