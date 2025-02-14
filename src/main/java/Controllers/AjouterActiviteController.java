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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TableView<Categories> categoryTable;
    @FXML
    private TableColumn<Categories, String> categoryNameColumn;
    @FXML
    private TableColumn<Categories, Void> updateColumn;
    @FXML
    private TableColumn<Categories, Void> deleteColumn;
    private final ServiceActivities serviceActivities = new ServiceActivities();
    private final ServiceCategory serviceCategory = new ServiceCategory();

    private AfficheActiviteController parentController;

    public void setParentController(AfficheActiviteController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void initialize() {
        loadCategories();
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        setupUpdateColumn();
        setupDeleteColumn();

    }
    private void setupUpdateColumn() {
        updateColumn.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button("Update");

            {
                button.setOnAction(event -> {
                    Categories category = getTableView().getItems().get(getIndex());
                    openUpdateCategoryWindow(category);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : button);
            }
        });
    }
    private void openUpdateCategoryWindow(Categories category) {
        try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateCategory.fxml"));
            Parent root = loader.load();

            UpdateCategoryController controller = loader.getController();
            controller.setCategory(category);
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadCategories(); // Refresh after update
        } catch (IOException e) {
            showAlert("Error", "Cannot open update window: " + e.getMessage());
        }
    }
    private void setupDeleteColumn() {
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button("Delete");

            {
                button.setOnAction(event -> {
                    Categories category = getTableView().getItems().get(getIndex());
                    try {
                        serviceCategory.supprimer(category.getId());
                        loadCategories(); // Refresh table
                    } catch (SQLException e) {
                        showAlert("Error", "Delete failed: " + e.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : button);
            }
        });
    }

    void loadCategories() {
        try {

            List<Categories> categories = serviceCategory.getAll();
            ObservableList<Categories> categoryList = FXCollections.observableArrayList(categories);
            categoryTable.setItems(categoryList);
            categoryTable.refresh();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load categories: " + e.getMessage());
        }
    }

    /**
     * Adds a new activity.
     */
    @FXML
    private void ajouterActivite() {
        try {
            // Validate required fields
            if (txtname.getText().isEmpty() || dpStartDate.getValue() == null || dpEndDate.getValue() == null || categoryTable.getSelectionModel().getSelectedItem() == null) {
                showAlert("Validation Error", "Please fill in all required fields.");
                return;
            }

            // Get values from the form
            String name = txtname.getText();
            String description = txtDescription.getText();
            LocalDateTime startDate = LocalDateTime.of(dpStartDate.getValue(), LocalTime.MIDNIGHT);
            LocalDateTime endDate = LocalDateTime.of(dpEndDate.getValue(), LocalTime.MIDNIGHT);
            String location = txtLocation.getText();
            Integer responsibleId = txtResponsibleId.getText().isEmpty() ? null : Integer.parseInt(txtResponsibleId.getText());

            // Get the selected category from the TableView
            Categories selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
            int categoryId = selectedCategory.getId();

            // Create and add the activity
            Activities activity = new Activities(1, name, description, startDate, endDate, location, responsibleId, categoryId);
            serviceActivities.ajouter(activity);

            // Refresh the parent controller's table
            if (parentController != null) {
                parentController.loadActivities();
            }

            // Close the window
            txtname.getScene().getWindow().hide();

        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while adding the activity: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Invalid Responsible ID. Please enter a number.");
        }
    }

    /**
     * Opens the Add Category window.
     */
    @FXML
    public void openAddCategoryWindow() {
        System.out.println("Button Clicked: Opening Add Category Window"); // Debug log
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
            e.printStackTrace(); // Print error to console
            showAlert("UI Error", "Failed to open the Add Category window: " + e.getMessage());
        }
    }

    /**
     * Displays an alert dialog.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
