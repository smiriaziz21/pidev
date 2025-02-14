package Entite;

import java.io.Serializable;
import java.time.LocalDate;

public class Event implements Serializable {
    private int id;
    private String title;
    private String description;
    private LocalDate date;
    private Integer capacity;
    private Integer hotelId;
    private Integer responsibleEventId;

    // Constructor
    public Event(int id, String title, String description, LocalDate date, Integer capacity, Integer hotelId, Integer responsibleEventId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.capacity = capacity;
        this.hotelId = hotelId;
        this.responsibleEventId = responsibleEventId;
    }

    // Getters and Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Integer getHotelId() { return hotelId; }
    public void setHotelId(Integer hotelId) { this.hotelId = hotelId; }

    public Integer getResponsibleEventId() { return responsibleEventId; }
    public void setResponsibleEventId(Integer responsibleEventId) { this.responsibleEventId = responsibleEventId; }

    @Override
    public String toString() {
        return "Event{" + "id=" + id + ", title='" + title + '\'' + ", description='" + description + '\'' +
                ", date=" + date + ", capacity=" + capacity + ", hotelId=" + hotelId +
                ", responsibleEventId=" + responsibleEventId + '}';
    }
}
