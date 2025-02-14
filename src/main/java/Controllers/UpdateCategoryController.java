package Controllers;

import Entite.Categories;
import Service.ServiceCategory;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;

public class UpdateCategoryController {
    @FXML
    private TextField txtCategoryName;
    @FXML
    private TextField txtCategoryDescription;

    private Categories category;
    private ServiceCategory serviceCategory = new ServiceCategory();
    private AjouterActiviteController parentController;

    public void setCategory(Categories category) {
        if (category != null) {
            this.category = category;
            txtCategoryName.setText(category.getName());
            txtCategoryDescription.setText(category.getDescription());
        } else {
            System.out.println("ERROR: Category is null in setCategory()");
        }
    }


    public void setParentController(AjouterActiviteController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void updateCategory() {
        try {
            category.setName(txtCategoryName.getText());
            category.setDescription(txtCategoryDescription.getText());

            System.out.println("Updating category: " + category.getName() + " " + category.getDescription());
            System.out.println("Category ID for update: " + category.getId()); // Debugging ID

            if (category.getId() != 0) {
                serviceCategory.update(category);
                System.out.println("Updated Category ID: " + category.getId());
            } else {
                showAlert("Invalid ID", "The category ID is invalid for the update.");
            }

            if (parentController != null) {
                parentController.loadCategories();  // Refresh the table
            }

            ((Stage) txtCategoryName.getScene().getWindow()).close();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to update category: " + e.getMessage());
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
