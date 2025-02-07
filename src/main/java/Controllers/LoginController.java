package Controllers;


import Entites.Users;
import Services.DaoLogin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private StackPane s;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;
    @FXML
    private ImageView eyeImage;
    private TextField visiblePasswordField;

    @FXML
    private void initialize() {
        //    eyeImage.setOnMouseClicked(event -> togglePasswordVisibility());

        visiblePasswordField = new TextField();
        visiblePasswordField.setManaged(false);
        visiblePasswordField.setVisible(false);
        visiblePasswordField.textProperty().bindBidirectional(password.textProperty());
        visiblePasswordField.promptTextProperty().bind(password.promptTextProperty());
        visiblePasswordField.setStyle("-fx-background-color: transparent; -fx-border-color: #0596ff; -fx-border-width: 0px 0px 2px 0px;");
        visiblePasswordField.setLayoutX(password.getLayoutX());
        visiblePasswordField.setLayoutY(password.getLayoutY());
        visiblePasswordField.setPrefSize(password.getPrefWidth(), password.getPrefHeight());
        s.getChildren().add(visiblePasswordField);
    }


    @FXML
    private void login(ActionEvent event) {
        String userEmail = email.getText();
        String userPassword = password.getText();

        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            showAlert("Error", "Email and password cannot be empty.", Alert.AlertType.ERROR);
            return;
        }

        Users user = DaoLogin.login(userEmail, userPassword);

        if (user != null) {
            Users.setCurrentUser(user);
            loadPageBasedOnRole(user.getRole().name());
        } else {
            showAlert("Login Failed", "Invalid email or password.", Alert.AlertType.ERROR);
        }
    }

    private void loadPageBasedOnRole(String role) {
        String fxmlFile;
        String title;

        switch (role) {
            case "client":
                fxmlFile = "/Signup.fxml";
                System.out.println("je suis "+role.toUpperCase()+"Client");
                title = "Client Dashboard";
                break;
            case "responsable_event":
                fxmlFile = "/AfficherPersonne.fxml";
                System.out.println("je suis "+role.toUpperCase()+"responsable_event");
                title = "Event Manager Dashboard";
                break;
            case "responsable_hotel":
                System.out.println("je suis "+role.toUpperCase()+"responsable_hotel");
                fxmlFile = "/HotelManagerDashboard.fxml";
                title = "Hotel Manager Dashboard";
                break;
            default:
                showAlert("Error", "Unknown role.", Alert.AlertType.ERROR);
                return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Users loggedInUser = Users.getCurrentUser();
            if (loggedInUser != null) {
                System.out.println("Utilisateur connecté : " + loggedInUser.getName() + " (ID: " + loggedInUser.getId() + ")");
            }
            Stage currentStage = (Stage) email.getScene().getWindow();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.setTitle(title);
            currentStage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load the page.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void alert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setHeaderText("Login Error");
        alert.setTitle("Login Error");

        alert.setOnCloseRequest(event -> email.requestFocus());


        alert.show();
    }

    @FXML
    private void signUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignUp.fxml"));
            Parent root = loader.load();
            SignUpController signUpController = loader.getController();


            Stage currentStage = (Stage) s.getScene().getWindow();


            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.setTitle("Sign Up");
            currentStage.show();
        } catch (IOException e) {
            alert("Failed to load SignUp.fxml", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }


}
