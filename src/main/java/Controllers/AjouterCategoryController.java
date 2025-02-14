package Controllers;

import Entite.Categories;
import Service.ServiceCategory;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AjouterCategoryController {

    @FXML
    private TextField txtCategoryName;

    private AjouterActiviteController parentController;

    public void setParentController(AjouterActiviteController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void ajouterCategory() {
        try {
            String categoryName = txtCategoryName.getText();
            if (categoryName == null || categoryName.isEmpty()) {
                showAlert("Error", "Category name cannot be empty.");
                return;
            }

            Categories category = new Categories(0, categoryName, "");
            ServiceCategory serviceCategory = new ServiceCategory();
            serviceCategory.ajouter(category);

            // Close the window
            Stage stage = (Stage) txtCategoryName.getScene().getWindow();
            stage.close();  // This closes the window
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while adding the category: " + e.getMessage());
        }
    }



    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}