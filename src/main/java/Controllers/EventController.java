package Controllers;

import Entite.Enum.Role;
import Entite.Event;
import Entite.Hotels;
import Entite.Users;
import Service.EventService;
import Service.HotelService;
import Service.ServicePersonne;
import Utils.DataSource;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EventController implements Initializable {
    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, Integer> idColumn;
    @FXML private TableColumn<Event, Integer> capacityColumn;
    @FXML private TableColumn<Event, String> titleColumn;
    @FXML private TableColumn<Event, String> descriptionColumn;
    @FXML private TableColumn<Event, Date> dateColumn;
    @FXML private ComboBox<String> responsableComboBox;
    @FXML private ComboBox<String> hotelComboBox;

    @FXML private TextField capacityField;
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private DatePicker datePicker;
    @FXML private TextField responsableField;
    @FXML private TableColumn<Event, String> responsableColumn;
    @FXML private TableColumn<Event, String> hotelColumn;
    @FXML private TableColumn<Event, Void> colActions;
    private EventService eventService;
    private ObservableList<Event> eventList;
    private Connection connection;
    private int responsableId; // Add this field

    public EventController() {
        this.connection = DataSource.getInstance().getCon();
        this.eventService = new EventService(connection);
        this.eventList = FXCollections.observableArrayList();
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        loadEvents();
        setupEventSelection();
        populateResponsables();
        populateHotels();
        setupActionButtons();

    }

    public void setResponsableId(int responsableId) {
        this.responsableId = responsableId;
        populateResponsables(); // Populate the responsableComboBox
        System.out.println(responsableId);
    }

    private void populateResponsables() {
        ServicePersonne service = ServicePersonne.getInstance();
        try {
            List<Users> responsables = service.getAll().stream()
                    .filter(u -> u.getRole() == Role.responsable_event)
                    .collect(Collectors.toList());

            // Add all responsables to the ComboBox
            responsables.forEach(u -> responsableComboBox.getItems().add(u.getName()));

            // Set the logged-in responsable's name as the default value
            Users loggedInResponsable = service.getById(responsableId);
            if (loggedInResponsable != null) {
                responsableComboBox.setValue(loggedInResponsable.getName());
                responsableComboBox.setDisable(true); // Disable the ComboBox to prevent changes
            }
        } catch (SQLException e) {
            showError("Error loading responsables", e.getMessage());
        }
    }

    private void populateHotels() {
        HotelService hotelService = new HotelService(DataSource.getInstance().getCon());
        try {
            List<Hotels> hotels = hotelService.getAll();
            hotels.forEach(h -> hotelComboBox.getItems().add(h.getName()));
        } catch (SQLException e) {
            showError("Error loading hotels", e.getMessage());
        }
    }

    private void setupTableColumns() {

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Convert Responsable ID to Name
        responsableColumn.setCellValueFactory(cellData -> {
            int responsableId = cellData.getValue().getResponsibleEventId();
            String responsableName = getResponsableNameById(responsableId);
            return new ReadOnlyObjectWrapper<>(responsableName); // ✅ Corrected return type
        });

        // Convert Hotel ID to Name
        hotelColumn.setCellValueFactory(cellData -> {
            int hotelId = cellData.getValue().getHotelId();
            String hotelName = getHotelNameById(hotelId);
            return new ReadOnlyObjectWrapper<>(hotelName); // ✅ Corrected return type
        });

        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        eventTable.setItems(eventList);
    }

    private void loadEvents() {
        Users loggedInUser = Users.getCurrentUser();
        if (loggedInUser == null) {
            showError("Error", "No user is logged in.");
            return;
        }

        try {
            eventList.clear();
            eventList.addAll(eventService.getByResponsableId(loggedInUser.getId())); // ✅ Load events only for the logged-in responsable
        } catch (SQLException e) {
            showError("Error loading events", e.getMessage());
        }
    }

    private void setupEventSelection() {
        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                titleField.setText(newSelection.getTitle());
                descriptionArea.setText(newSelection.getDescription());
                datePicker.setValue(newSelection.getDate());

                responsableField.setText(String.valueOf(newSelection.getResponsibleEventId()));
            }
        });
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        try {
            Event newEvent = createEventFromInputs();

            if (newEvent == null) {
                showError("Error", "Failed to create event from inputs.");
                return;
            }

            System.out.println("Adding event: " + newEvent.toString());

            eventService.add(newEvent);
            loadEvents();
            clearInputs();
        } catch (SQLException e) {
            showError("Error adding event", e.getMessage());
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showError("Error", "Please select an event to update");
            return;
        }

        try {
            Event updatedEvent = createEventFromInputs();
            updatedEvent.setId(selectedEvent.getId());
            eventService.update(updatedEvent);
            loadEvents();
            clearInputs();
        } catch (SQLException e) {
            showError("Error updating event", e.getMessage());
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showError("Error", "Please select an event to delete");
            return;
        }

        try {
            eventService.delete(selectedEvent.getId());
            loadEvents();
            clearInputs();
        } catch (SQLException e) {
            showError("Error deleting event", e.getMessage());
        }
    }

    private int getResponsableIdByName(String name) {
        try {
            return eventService.getResponsableIdByName(name);
        } catch (SQLException e) {
            showError("Error", "Could not fetch responsable ID.");
            return -1;
        }
    }

    private int getHotelIdByName(String name) {
        try {
            return eventService.getHotelIdByName(name);
        } catch (SQLException e) {
            showError("Error", "Could not fetch hotel ID.");
            return -1;
        }
    }

    private Event createEventFromInputs() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        LocalDate date = datePicker.getValue();
        String hotelName = hotelComboBox.getValue();

        if (title.isEmpty() || description.isEmpty() || date == null || hotelName == null) {
            showError("Error", "All fields must be filled.");
            return null;
        }

        int hotelId = getHotelIdByName(hotelName);
        int capacity = Integer.parseInt(capacityField.getText());

        // Use the logged-in responsable's ID
        return new Event(0, title, description, date, capacity, hotelId, responsableId);
    }



    private void clearInputs() {
        titleField.clear();
        descriptionArea.clear();
        datePicker.setValue(null);
        responsableField.clear();
        eventTable.getSelectionModel().clearSelection();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private String getResponsableNameById(int responsableId) {
        try {
            ServicePersonne service = ServicePersonne.getInstance();
            Users user = service.getById(responsableId);
            return (user != null) ? user.getName() : "Unknown"; // ✅ Avoid NullPointerException
        } catch (SQLException e) {
            System.out.println("Error fetching responsable name: " + e.getMessage());
            return "Error";
        }
    }

    private String getHotelNameById(int hotelId) {
        try {
            HotelService hotelService = new HotelService(DataSource.getInstance().getCon());
            Hotels hotel = hotelService.getById(hotelId);
            return (hotel != null) ? hotel.getName() : "Unknown"; // ✅ Avoid NullPointerException
        } catch (SQLException e) {
            System.out.println("Error fetching hotel name: " + e.getMessage());
            return "Error";
        }
    }
    private void setupActionButtons() {
        // Define a cell factory for the "Actions" column
        Callback<TableColumn<Event, Void>, TableCell<Event, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnActivities = new Button("Activities");

            {
                // Set button action to open the Activities interface
                btnActivities.setOnAction(event -> {
                    Event eventData = getTableView().getItems().get(getIndex());

                    openActivitiesInterface(eventData.getId(),eventData.getResponsibleEventId());
                });

                // Style the button
                btnActivities.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnActivities);
                }
            }
        };

        // Set the cell factory for the "Actions" column
        colActions.setCellFactory(cellFactory);
    }

    private void openActivitiesInterface(int eventId, int responsableId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheActivities.fxml"));
            Parent root = loader.load();

            // Pass the event ID and responsable ID to the Activities controller
            AfficheActiviteController controller = loader.getController();
            controller.setEventId(eventId);
            controller.setResponsableId(responsableId); // Pass the responsable ID

            // Open the Activities interface in a new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Activities for Event ID: " + eventId);
            stage.show();
        } catch (IOException e) {
            showError("Error opening Activities interface", e.getMessage());
        }
    }
}
