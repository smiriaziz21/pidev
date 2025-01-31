package Entite;

import java.util.Date;

public class Event {
    private int id;
    private String title;
    private String description;
    private Date date;
    private int responsableEventId;

    public Event(int id, String title, String description, Date date, int responsableEventId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.responsableEventId = responsableEventId;
    }

    public Event(String title, String description, Date date, int responsableEventId) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.responsableEventId = responsableEventId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public int getResponsableEventId() { return responsableEventId; }
    public void setResponsableEventId(int responsableEventId) { this.responsableEventId = responsableEventId; }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", responsableEventId=" + responsableEventId +
                '}';
    }
}
