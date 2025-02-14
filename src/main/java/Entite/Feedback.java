package Entite;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Feedback {

    private IntegerProperty id;
    private IntegerProperty clientId;
    private IntegerProperty eventId;
    private StringProperty comment;
    private IntegerProperty rating;
    private ObjectProperty<LocalDate> date;

    public Feedback(int clientId, int eventId, String comment, int rating, LocalDate date) {
        this.id = new SimpleIntegerProperty(0);
        this.clientId = new SimpleIntegerProperty(clientId);
        this.eventId = new SimpleIntegerProperty(eventId);
        this.comment = new SimpleStringProperty(comment);
        this.rating = new SimpleIntegerProperty(rating);
        this.date = new SimpleObjectProperty<>(date);
    }

    public Feedback(int id, int clientId, int eventId, String comment, int rating, LocalDate date) {
        this.id = new SimpleIntegerProperty(id);
        this.clientId = new SimpleIntegerProperty(clientId);
        this.eventId = new SimpleIntegerProperty(eventId);
        this.comment = new SimpleStringProperty(comment);
        this.rating = new SimpleIntegerProperty(rating);
        this.date = new SimpleObjectProperty<>(date);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getClientId() {
        return clientId.get();
    }

    public void setClientId(int clientId) {
        this.clientId.set(clientId);
    }

    public IntegerProperty clientIdProperty() {
        return clientId;
    }

    public int getEventId() {
        return eventId.get();
    }

    public void setEventId(int eventId) {
        this.eventId.set(eventId);
    }

    public IntegerProperty eventIdProperty() {
        return eventId;
    }

    public String getComment() {
        return comment.get();
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public int getRating() {
        return rating.get();
    }

    public void setRating(int rating) {
        this.rating.set(rating);
    }

    public IntegerProperty ratingProperty() {
        return rating;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public boolean isWithinDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && date.get().isBefore(startDate)) {
            return false;
        }
        if (endDate != null && date.get().isAfter(endDate)) {
            return false;
        }
        return true;
    }

    public boolean matchesSearchKeyword(String keyword) {
        return comment.get().toLowerCase().contains(keyword.toLowerCase());
    }
}
