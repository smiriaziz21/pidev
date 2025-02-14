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

    private int currentResponsableId;

    public void setResponsableId(int currentResponsableId) {
        this.currentResponsableId = currentResponsableId;
    }

    @FXML
    public void initialize() {
        configureButtonActions();
    }

    private void configureButtonActions() {
        btnAjouterHotel.setOnAction(e -> loadDataPage("ajouterhotel.fxml", "hotel"));
        btnModifierHotel.setOnAction(e -> loadDataPage("modifierhotel.fxml", "hotel"));
        btnSupprimerHotel.setOnAction(e -> loadDataPage("suprimerhotel.fxml", "hotel"));
        btnAjouterChambre.setOnAction(e -> loadDataPage("ajoutchambre.fxml", "hotel"));
    }



    private void loadDataPage(String fxmlFile, String type) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlFile));
            Parent root = loader.load();

            if (loader.getController() instanceof ModifierHotelController) {
                ((ModifierHotelController) loader.getController())
                        .setResponsableId(currentResponsableId);
            } else if (loader.getController() instanceof AjouterHotelController) {
                ((AjouterHotelController) loader.getController())
                        .setResponsableId(currentResponsableId);
            } else if (loader.getController() instanceof SuprimerHotelController) {
                ((SuprimerHotelController) loader.getController())
                        .setResponsableId(currentResponsableId);

            } else if (loader.getController() instanceof Suprimerchambre) {
                ((Suprimerchambre) loader.getController())
                        .setResponsableId(currentResponsableId);
            }else if (loader.getController() instanceof Ajouterunechambre) {
                ((Ajouterunechambre) loader.getController())
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