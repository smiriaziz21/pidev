package Entite;

public class Reservation {
    private int id;
    private int clientId;
    private int eventId;
    private String status;

    public Reservation(int id, int clientId, int eventId, String status) {
        this.id = id;
        this.clientId = clientId;
        this.eventId = eventId;
        this.status = status;
    }

    public Reservation(int clientId, int eventId, String status) {
        this.clientId = clientId;
        this.eventId = eventId;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", eventId=" + eventId +
                ", status='" + status + '\'' +
                '}';
    }
}
