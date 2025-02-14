package Controllers;

import Utils.DataSource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ResponsableEventDashboard {
    // Déclaration des composants FXML
    @FXML
    private Button btnEventMan;
    @FXML private Button btnRes;

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
        btnEventMan.setOnAction(e -> loadDataPage("EventManagement.fxml"));
        btnRes.setOnAction(e -> loadDataPage("AdminReservation.fxml"));

    }



    private void loadDataPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlFile));
            Parent root = loader.load();

            if (loader.getController() instanceof EventController) {
                ((EventController) loader.getController())
                        .setResponsableId(currentResponsableId);
            } else if (loader.getController() instanceof AdminReservationsController) {
                ((AdminReservationsController) loader.getController())
                        .setResponsableId(currentResponsableId);
                ((AdminReservationsController) loader.getController()).setConnection(DataSource.getInstance().getCon());
                ((AdminReservationsController) loader.getController()).initController();
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
        this.currentResponsableId = id;
    }
}
