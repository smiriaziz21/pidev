package Controllers;

import Entite.Reservation;
import Service.ReservationService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.sql.Connection;
import java.sql.SQLException;

public class AdminReservationsController {

    @FXML private TableView<Reservation> reservationTable;

    private ReservationService reservationService;
    private Connection connection; // Store the database connection
    private int responsableId; // Add this field to store the responsable ID

    // ✅ Add this missing method
    public void setConnection(Connection connection) {
        this.connection = connection;
        this.reservationService = new ReservationService(connection); // Initialize reservationService
    }

    // ✅ Add this method to set the responsable ID
    public void setResponsableId(int responsableId) {
        this.responsableId = responsableId;
    }

    // ✅ Custom method to initialize the controller
    public void initController() {
        if (reservationService == null) {
            throw new IllegalStateException("reservationService is not initialized. Call setConnection() first.");
        }
        setupTable();
        loadReservations();
    }

    private void setupTable() {
        // Bind the client name column
        TableColumn<Reservation, String> clientNameColumn = new TableColumn<>("Client Name");
        clientNameColumn.setCellValueFactory(cellData -> {
            int clientId = cellData.getValue().getClientId();
            try {
                String clientName = reservationService.getClientNameById(clientId);
                return new ReadOnlyObjectWrapper<>(clientName);
            } catch (SQLException e) {
                return new ReadOnlyObjectWrapper<>("Error");
            }
        });

        // Bind the event title column
        TableColumn<Reservation, String> eventTitleColumn = new TableColumn<>("Event Title");
        eventTitleColumn.setCellValueFactory(cellData -> {
            int eventId = cellData.getValue().getEventId();
            try {
                String eventTitle = reservationService.getEventTitleById(eventId);
                return new ReadOnlyObjectWrapper<>(eventTitle);
            } catch (SQLException e) {
                return new ReadOnlyObjectWrapper<>("Error");
            }
        });

        // Bind the status column
        TableColumn<Reservation, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Add actions column
        TableColumn<Reservation, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button accept = new Button("Accept");
            private final Button reject = new Button("Reject");

            {
                accept.setOnAction(event -> updateStatus("approved"));
                reject.setOnAction(event -> updateStatus("rejected"));
            }

            private void updateStatus(String status) {
                Reservation res = getTableView().getItems().get(getIndex());
                try {
                    reservationService.updateReservationStatus(res.getId(), status);
                    reservationTable.refresh();
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR, "Update failed: " + e.getMessage()).show();
                }
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(5, accept, reject));
            }
        });

        // Clear existing columns and add the new ones
        reservationTable.getColumns().clear();
        reservationTable.getColumns().addAll(clientNameColumn, eventTitleColumn, statusColumn, actionsColumn);
    }

    private void loadReservations() {
        try {
            // Fetch reservations for the logged-in responsable
            reservationTable.setItems(FXCollections.observableArrayList(
                    reservationService.getReservationsByResponsable(responsableId)
            ));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading reservations: " + e.getMessage()).show();
        }
    }

}