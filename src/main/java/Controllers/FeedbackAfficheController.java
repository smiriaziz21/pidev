package Controllers;

import Entite.Feedback;
import Service.ServiceFeedback;  // You'll need to create this service for feedback
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FeedbackAfficheController implements Initializable {

    @FXML
    private TableView<Feedback> tablev;

    @FXML
    private TableColumn<Feedback, Integer> colId;

    @FXML
    private TableColumn<Feedback, Integer> colClientId;

    @FXML
    private TableColumn<Feedback, Integer> colEventId;

    @FXML
    private TableColumn<Feedback, String> colComment;

    @FXML
    private TableColumn<Feedback, Integer> colRating;

    @FXML
    private TableColumn<Feedback, String> colDate;

    @FXML
    private TableColumn<Feedback, Void> colActions;

    @FXML
    private Button btnAjouter;

    @FXML
    private TextField searchField;

    @FXML
    private DatePicker dateDebut;  // Start Date Picker
    @FXML
    private DatePicker dateFin;    // End Date Picker

    private final ServiceFeedback service = new ServiceFeedback();
    private ObservableList<Feedback> feedbackList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadFeedbacks();
        setupActionButtons();

        btnAjouter.setOnMouseEntered(event -> btnAjouter.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white;"));
        btnAjouter.setOnMouseExited(event -> btnAjouter.setStyle("-fx-background-color: #43a047; -fx-text-fill: white;"));

        // Add listeners to filters
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterFeedbacks());
        dateDebut.valueProperty().addListener((observable, oldValue, newValue) -> filterFeedbacks());  // Add listener for start date
        dateFin.valueProperty().addListener((observable, oldValue, newValue) -> filterFeedbacks());    // Add listener for end date
    }

    // Method to load all feedbacks from the service
    private void loadFeedbacks() {
        try {
            feedbackList.setAll(service.getAll());
            System.out.println("Feedback List Size: " + feedbackList.size()); // Debugging line
            tablev.setItems(feedbackList);

            colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
            colClientId.setCellValueFactory(cellData -> cellData.getValue().clientIdProperty().asObject());
            colEventId.setCellValueFactory(cellData -> cellData.getValue().eventIdProperty().asObject());
            colComment.setCellValueFactory(cellData -> cellData.getValue().commentProperty());
            colRating.setCellValueFactory(cellData -> cellData.getValue().ratingProperty().asObject());

            // Format date as string
            colDate.setCellValueFactory(cellData -> {
                if (cellData.getValue().getDate() != null) {
                    return new SimpleStringProperty(cellData.getValue().getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } else {
                    return new SimpleStringProperty("");
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to filter feedbacks based on search and date range
    private void filterFeedbacks() {
        String keyword = searchField.getText().toLowerCase();
        LocalDate startDate = dateDebut.getValue();
        LocalDate endDate = dateFin.getValue();

        ObservableList<Feedback> filteredList = feedbackList.stream()
                .filter(feedback -> {
                    // Check keyword in various properties
                    boolean matchesKeyword = keyword.isEmpty() ||
                            String.valueOf(feedback.getId()).contains(keyword) ||
                            String.valueOf(feedback.getClientId()).contains(keyword) ||
                            String.valueOf(feedback.getEventId()).contains(keyword) ||
                            feedback.getComment().toLowerCase().contains(keyword) ||
                            (feedback.getDate() != null && feedback.getDate().toString().contains(keyword));

                    // Check if date is within the selected range
                    boolean matchesDate = (startDate == null || feedback.getDate() != null && !feedback.getDate().isBefore(startDate)) &&
                            (endDate == null || feedback.getDate() != null && !feedback.getDate().isAfter(endDate));

                    return matchesKeyword && matchesDate;
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        tablev.setItems(filteredList);
    }

    // Method to setup actions for Update/Delete buttons
    private void setupActionButtons() {
        Callback<TableColumn<Feedback, Void>, TableCell<Feedback, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnUpdate = new Button("Update");
            private final Button btnDelete = new Button("Delete");

            {
                // Update button action
                btnUpdate.setOnAction(event -> {
                    Feedback feedback = getTableView().getItems().get(getIndex());
                    openUpdateWindow(feedback);
                });
                // Delete button action
                btnDelete.setOnAction(event -> {
                    Feedback feedback = getTableView().getItems().get(getIndex());
                    deleteFeedback(feedback);
                });

                // Button styles
                btnUpdate.setStyle("-fx-background-color: #ffa726; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
                btnDelete.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10, btnUpdate, btnDelete);
                    setGraphic(hbox);
                }
            }
        };

        colActions.setCellFactory(cellFactory);
    }

    // Method to handle feedback deletion
    private void deleteFeedback(Feedback feedback) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to delete this feedback?");
        alert.setContentText("This action cannot be undone!");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    service.supprimer(feedback);
                    feedbackList.remove(feedback);
                    filterFeedbacks(); // Update the list after deletion
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Method to open the update window for a selected feedback
    private void openUpdateWindow(Feedback feedback) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FeedbackModifier.fxml"));
            Parent root = loader.load();

            FeedbackModifierController controller = loader.getController();
            controller.initData(feedback);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update Feedback");
            stage.showAndWait();

            loadFeedbacks();
            filterFeedbacks(); // Ensure the list is updated after modification
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to open the add window for a new feedback
    public void openAddWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FeedbackAjouter.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Feedback");
            stage.showAndWait();

            loadFeedbacks();
            filterFeedbacks(); // Update the list after adding
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Back button action
    @FXML
    private void Back(ActionEvent event) {
        System.out.println("Back to previous screen");
    }
}
