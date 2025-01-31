package Controllers;

import Entites.Enum.Role;
import Entites.Users;
import Services.ServicePersonne;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private AnchorPane s;
    @FXML
    private TextField emailField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signUpButton;
    @FXML
    private Button backToLogin;
    @FXML
    private CheckBox clientCheckBox;
    @FXML
    private CheckBox eventManagerCheckBox;
    @FXML
    private CheckBox hotelManagerCheckBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureCheckBoxBehaviour();
    }

    private void configureCheckBoxBehaviour() {
        clientCheckBox.setOnAction(event -> deselectOthers(clientCheckBox));
        eventManagerCheckBox.setOnAction(event -> deselectOthers(eventManagerCheckBox));
        hotelManagerCheckBox.setOnAction(event -> deselectOthers(hotelManagerCheckBox));
    }

    private void deselectOthers(CheckBox selectedCheckBox) {
        if (selectedCheckBox.isSelected()) {
            clientCheckBox.setSelected(selectedCheckBox == clientCheckBox);
            eventManagerCheckBox.setSelected(selectedCheckBox == eventManagerCheckBox);
            hotelManagerCheckBox.setSelected(selectedCheckBox == hotelManagerCheckBox);
        }
    }

    @FXML
    private void signUp(ActionEvent event) {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        Role role = getSelectedRole();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || role == null) {
            showAlert("Veuillez remplir tous les champs et sélectionner un rôle.", Alert.AlertType.WARNING);
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Veuillez entrer une adresse email valide.", Alert.AlertType.WARNING);
            return;
        }

        Users newUser = new Users(0, username, email, password, role);

        try {
            boolean success = ServicePersonne.getInstance().ajouter(newUser);
            if (success) {
                showAlert("Inscription réussie !", Alert.AlertType.INFORMATION);
                clearFields();
            } else {
                showAlert("Erreur lors de l'inscription. Veuillez réessayer.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Erreur système : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void backToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Login");
            currentStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println(e.getCause());
            showAlert("Impossible de charger la page de connexion.", Alert.AlertType.ERROR);
        }
    }

    private Role getSelectedRole() {
        if (clientCheckBox.isSelected()) {
            return Role.client;
        } else if (eventManagerCheckBox.isSelected()) {
            return Role.responsable_event;
        } else if (hotelManagerCheckBox.isSelected()) {
            return Role.responsable_hotel;
        } else {
            return null;
        }
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    private void clearFields() {
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        clientCheckBox.setSelected(false);
        eventManagerCheckBox.setSelected(false);
        hotelManagerCheckBox.setSelected(false);
    }
}
