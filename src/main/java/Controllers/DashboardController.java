package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class DashboardController {

    // Déclaration des composants FXML
    @FXML private Button btnAjouterHotel;
    @FXML private Button btnModifierHotel;
    @FXML private Button btnSupprimerHotel;
    @FXML private Button btnAjouterChambre;
    @FXML private AnchorPane mainContent;

    private int currentResponsableId = 1; // static badalha

    @FXML
    public void initialize() {
        configureButtonActions();
    }

    private void configureButtonActions() {
        btnAjouterHotel.setOnAction(e -> loadPage("ajouterhotel.fxml"));
        btnModifierHotel.setOnAction(e -> loadDataPage("modifierhotel.fxml", "hotel"));
        btnSupprimerHotel.setOnAction(e -> loadPage("suprimerhotel.fxml"));
        btnAjouterChambre.setOnAction(e -> loadPage("ajoutchambre.fxml"));
    }

    private void loadPage(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/" + fxmlFile));
            mainContent.getChildren().setAll(root);
        } catch (IOException | NullPointerException e) {
            showErrorAlert("Fichier manquant",
                    "Le fichier " + fxmlFile + " est introuvable !");
        }
    }

    private void loadDataPage(String fxmlFile, String type) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlFile));
            Parent root = loader.load();

            if(loader.getController() instanceof ModifierHotelController) {
                ((ModifierHotelController)loader.getController())
                        .setResponsableId(currentResponsableId);
            }

            mainContent.getChildren().setAll(root);

        } catch (IOException e) {
            showErrorAlert("Erreur de chargement",
                    "Échec du chargement de " + fxmlFile);
        }
    }

    private void showErrorAlert(String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setCurrentResponsableId(int id) {
        this.currentResponsableId = id;
    }
}
