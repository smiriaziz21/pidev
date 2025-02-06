package Entite;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Facture {

    private IntegerProperty id;
    private IntegerProperty reservationId;
    private DoubleProperty amount;
    private ObjectProperty<LocalDate> date;

    // Constructor for new Facture without ID (ID will be set to 0)
    public Facture(int reservationId, double amount, LocalDate date) {
        this.id = new SimpleIntegerProperty(0);  // Set default ID to 0
        this.reservationId = new SimpleIntegerProperty(reservationId);
        this.amount = new SimpleDoubleProperty(amount);
        this.date = new SimpleObjectProperty<>(date);
    }

    // Constructor for existing Facture with ID (used when loading from DB)
    public Facture(int id, int reservationId, double amount, LocalDate date) {
        this.id = new SimpleIntegerProperty(id);
        this.reservationId = new SimpleIntegerProperty(reservationId);
        this.amount = new SimpleDoubleProperty(amount);
        this.date = new SimpleObjectProperty<>(date);
    }

    // Getters and Setters for the properties
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getReservationId() {
        return reservationId.get();
    }

    public void setReservationId(int reservationId) {
        this.reservationId.set(reservationId);
    }

    public IntegerProperty reservationIdProperty() {
        return reservationId;
    }

    public double getAmount() {
        return amount.get();
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public DoubleProperty amountProperty() {
        return amount;
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
}
