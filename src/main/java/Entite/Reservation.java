package Entite;

import javafx.beans.property.*;

public class Reservation {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty clientId = new SimpleIntegerProperty();
    private final IntegerProperty eventId = new SimpleIntegerProperty();
    private final StringProperty status = new SimpleStringProperty();

    public Reservation(int id, int clientId, int eventId, String status) {
        setId(id);
        setClientId(clientId);
        setEventId(eventId);
        setStatus(status);
    }

    public Reservation(int clientId, int eventId, String status) {
        setClientId(clientId);
        setEventId(eventId);
        setStatus(status);
    }

    // Property getters
    public IntegerProperty idProperty() { return id; }
    public IntegerProperty clientIdProperty() { return clientId; }
    public IntegerProperty eventIdProperty() { return eventId; }
    public StringProperty statusProperty() { return status; }

    // Regular getters
    public int getId() { return id.get(); }
    public int getClientId() { return clientId.get(); }
    public int getEventId() { return eventId.get(); }
    public String getStatus() { return status.get(); }

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setClientId(int id) { this.clientId.set(id); }
    public void setEventId(int id) { this.eventId.set(id); }
    public void setStatus(String status) { this.status.set(status); }
}