package Controllers;

import Entite.Activities;
import Entite.Categories;
import Service.ServiceActivities;
import Service.ServiceCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class AjouterActiviteController {

    @FXML
    private TextField txtname;
    @FXML
    private TextArea txtDescription;
    @FXML
    private DatePicker dpStartDate;
    @FXML
    private DatePicker dpEndDate;
    @FXML
    private TextField txtLocation;
    @FXML
    private TextField txtResponsibleId;
    @FXML
    private ComboBox<String> cbCategory;

    private final ServiceActivities serviceActivities = new ServiceActivities();
    private final ServiceCategory serviceCategory = new ServiceCategory();

    private AfficheActiviteController parentController;

    public void setParentController(AfficheActiviteController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void initialize() {
        loadCategories();
    }

    private void loadCategories() {
        try {
            List<Categories> categories = serviceCategory.getAll();
            ObservableList<String> categoryNames = FXCollections.observableArrayList();
            for (Categories category : categories) {
                categoryNames.add(category.getName());
            }
            cbCategory.setItems(categoryNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ajouterActivite() {
        try {
            // Get values from the form
            String name = txtname.getText();
            String description = txtDescription.getText();
            LocalDateTime startDate = LocalDateTime.of(dpStartDate.getValue(), LocalTime.MIDNIGHT);
            LocalDateTime endDate = LocalDateTime.of(dpEndDate.getValue(), LocalTime.MIDNIGHT);
            String location = txtLocation.getText();
            Integer responsibleId = txtResponsibleId.getText().isEmpty() ? null : Integer.parseInt(txtResponsibleId.getText());

            // Handle dynamic category
            String categoryName = cbCategory.getValue();
            if (categoryName == null || categoryName.isEmpty()) {
                showAlert("Category Error", "Category cannot be empty. Please select or enter a category.");
                return;
            }

            int categoryId;
            // Check if the category already exists
            Categories category = serviceCategory.findByName(categoryName);
            if (category == null) {
                // Add new category
                category = new Categories(categoryName, "");
                serviceCategory.ajouter(category);
                categoryId = category.getId();
            } else {
                categoryId = category.getId();
            }

            // Create and add the activity
            Activities activity = new Activities(1, name, description, startDate, endDate, location, responsibleId, categoryId);
            serviceActivities.ajouter(activity);

            // Refresh the parent controller's table
            if (parentController != null) {
                parentController.loadActivities();
            }

            // Close the window
            txtname.getScene().getWindow().hide();
        } catch (SQLException | IllegalArgumentException e) {
            showAlert("Error", "An error occurred while adding the activity: " + e.getMessage());
        }
    }

    @FXML
    private void openAddCategoryWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCategory.fxml"));
            Parent root = loader.load();

            AjouterCategoryController controller = loader.getController();
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Category");
            stage.showAndWait();

            // Refresh categories after adding
            loadCategories();
        } catch (IOException e) {
            showAlert("Error", "Failed to open the Add Category window: " + e.getMessage());
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