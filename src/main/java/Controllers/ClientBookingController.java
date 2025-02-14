package Controllers;

import Entite.Event;
import Entite.Reservation;
import Service.EventService;
import Service.ReservationService;
import Utils.DataSource;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class ClientBookingController {
    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, Integer> idColumn;
    @FXML private TableColumn<Event, String> titleColumn;
    @FXML private TableColumn<Event, String> dateColumn;
    @FXML private TableColumn<Event, String> responsableColumn;
    @FXML private TableColumn<Event, String> hotelColumn;
    @FXML private TableColumn<Event, Void> actionsColumn; // ✅ New Column for "View Activities"

    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, Integer> eventIdColumn;
    @FXML private TableColumn<Reservation, String> statusColumn;
    @FXML private Button bookButton;
    @FXML private Button showWeatherButton; // ✅ New Button for Weather

    private EventService eventService;
    private ReservationService reservationService;
    private int clientId;

    public ClientBookingController() {
        Connection conn = DataSource.getInstance().getCon();
        this.eventService = new EventService(conn);
        this.reservationService = new ReservationService(conn);
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @FXML
    public void initialize() {
        setupEventTable();
        setupReservationTable();
        loadEvents();
        loadReservations();

        // ✅ Add event handler for "Show Weather" button
        showWeatherButton.setOnAction(event -> handleShowWeather());
    }

    private void setupEventTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // ✅ Show Responsable Name instead of ID
        responsableColumn.setCellValueFactory(cellData -> {
            int responsableId = cellData.getValue().getResponsibleEventId();
            String responsableName = getResponsableNameById(responsableId);
            return new ReadOnlyObjectWrapper<>(responsableName);
        });

        // ✅ Show Hotel Name instead of ID
        hotelColumn.setCellValueFactory(cellData -> {
            int hotelId = cellData.getValue().getHotelId();
            String hotelName = getHotelNameById(hotelId);
            return new ReadOnlyObjectWrapper<>(hotelName);
        });

        // ✅ Add "View Activities" Button
        actionsColumn.setCellFactory(getActivityButtonCellFactory());

        eventTable.setItems(FXCollections.observableArrayList());
    }

    private void setupReservationTable() {
        // Replace eventIdColumn with eventTitleColumn
        TableColumn<Reservation, String> eventTitleColumn = new TableColumn<>("Event Title");

        // Use a cell value factory to fetch the event title by ID
        eventTitleColumn.setCellValueFactory(cellData -> {
            int eventId = cellData.getValue().getEventId();
            try {
                String eventTitle = eventService.getEventTitleById(eventId);
                return new ReadOnlyObjectWrapper<>(eventTitle);
            } catch (SQLException e) {
                return new ReadOnlyObjectWrapper<>("Error");
            }
        });

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Clear existing columns and add the new ones
        reservationTable.getColumns().clear();
        reservationTable.getColumns().addAll(eventTitleColumn, statusColumn);
    }

    private void loadEvents() {
        try {
            eventTable.setItems(FXCollections.observableArrayList(eventService.getAll()));
        } catch (SQLException e) {
            showAlert("Error loading events: " + e.getMessage());
        }
    }

    private void loadReservations() {
        try {
            // Fetch only the reservations for the logged-in client
            reservationTable.setItems(FXCollections.observableArrayList(
                    reservationService.getClientReservations(clientId)
            ));
        } catch (SQLException e) {
            showAlert("Error loading reservations: " + e.getMessage());
        }
    }

    @FXML
    private void handleBookEvent() {
        Event selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error: No event selected.");
            return;
        }

        try {
            // Automatically pass the client's ID when booking
            reservationService.addReservation(new Reservation(clientId, selected.getId(), "pending"));
            loadReservations(); // Refresh the reservation table
        } catch (SQLException e) {
            showAlert("Booking failed: " + e.getMessage());
        }
    }

    // ✅ Handle "Show Weather" Button Click
    @FXML
    private void handleShowWeather() {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert("Error: No event selected.");
            return;
        }

        try {
            // Get the hotel location and event date
            String hotelLocation = getHotelLocationById(selectedEvent.getHotelId());
            LocalDate eventDate =selectedEvent.getDate();

            // Open the WeatherController interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/weather.fxml"));
            Parent root = loader.load();

            WeatherController weatherController = loader.getController();
            weatherController.setLocationAndDate(hotelLocation, eventDate);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Weather for Event: " + selectedEvent.getTitle());
            stage.show();
        } catch (IOException e) {
            showAlert("Error opening Weather interface: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    // ✅ Helper method to get Responsable Name by ID
    private String getResponsableNameById(int responsableId) {
        try {
            return eventService.getResponsableNameById(responsableId);
        } catch (Exception e) {
            return "Unknown";
        }
    }

    // ✅ Helper method to get Hotel Name by ID
    private String getHotelNameById(int hotelId) {
        try {
            return eventService.getHotelNameById(hotelId);
        } catch (Exception e) {
            return "Unknown";
        }
    }private String getHotelLocationById(int hotelId) {
        try {
            return eventService.getHotelLocationById(hotelId);
        } catch (Exception e) {
            return "Unknown";
        }
    }

    // ✅ Factory for "View Activities" Button
    private Callback<TableColumn<Event, Void>, TableCell<Event, Void>> getActivityButtonCellFactory() {
        return param -> new TableCell<>() {
            private final Button btnActivities = new Button("View Activities");

            {
                btnActivities.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

                btnActivities.setOnAction(event -> {
                    Event eventData = getTableView().getItems().get(getIndex());
                    openActivitiesInterface(eventData.getId());
                });
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
    }

    private void openActivitiesInterface(int eventId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/activite_client_view.fxml"));
            Parent root = loader.load();

            ActiviteClientView controller = loader.getController();
            controller.setEventId(eventId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Activities for Event ID: " + eventId);
            stage.show();
        } catch (IOException e) {
            showAlert("Error opening Activities interface: " + e.getMessage());
        }
    }
}