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
    private int categoryId;

    public Activities(int id, int idEvent, String name, String description, LocalDateTime startDate, LocalDateTime endDate, String location, Integer responsibleId, int categoryId) {
        this.id = id;
        this.idEvent = idEvent;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.responsibleId = responsibleId;
        this.categoryId = categoryId;
    }

    public Activities(int idEvent, String name, String description, LocalDateTime startDate, LocalDateTime endDate, String location, Integer responsibleId, int categoryId) {
        this.idEvent = idEvent;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.responsibleId = responsibleId;
        this.categoryId = categoryId;
    }

    public Activities() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdEvent() { return idEvent; }
    public void setIdEvent(int idEvent) { this.idEvent = idEvent; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getResponsibleId() { return responsibleId; }
    public void setResponsibleId(Integer responsibleId) { this.responsibleId = responsibleId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
}