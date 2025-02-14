package Entite;

import java.time.LocalDateTime;

public class Activities {
    private int id;
    private int idEvent;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private Integer responsibleId;


    public Activities(int id, int idEvent, String name, String description, LocalDateTime startDate, LocalDateTime endDate, String location, Integer responsibleId) {
        this.id = id;
        this.idEvent = idEvent;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.responsibleId = responsibleId;
    }


    public Activities(int idEvent, String name, String description, LocalDateTime startDate, LocalDateTime endDate, String location, Integer responsibleId) {
        this.idEvent = idEvent;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.responsibleId = responsibleId;
    }


    public Activities() {

    }



    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getResponsibleId() {
        return responsibleId;
    }

    public void setResponsibleId(Integer responsibleId) {
        this.responsibleId = responsibleId;
    }

    // Optional: Add method for converting LocalDateTime to SQL Timestamp if needed for database storage
}
