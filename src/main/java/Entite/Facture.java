package Entite;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Facture {

    private IntegerProperty id;
    private IntegerProperty reservationId;
    private DoubleProperty amount;
    private ObjectProperty<LocalDate> date;
    private StringProperty conditionDePaiement;
    private StringProperty modeDePaiement;

    public Facture(int reservationId, double amount, LocalDate date, String conditionDePaiement, String modeDePaiement) {
        this.id = new SimpleIntegerProperty(0);
        this.reservationId = new SimpleIntegerProperty(reservationId);
        this.amount = new SimpleDoubleProperty(amount);
        this.date = new SimpleObjectProperty<>(date);
        this.conditionDePaiement = new SimpleStringProperty(conditionDePaiement);
        this.modeDePaiement = new SimpleStringProperty(modeDePaiement);
    }

    public Facture(int id, int reservationId, double amount, LocalDate date, String conditionDePaiement, String modeDePaiement) {
        this.id = new SimpleIntegerProperty(id);
        this.reservationId = new SimpleIntegerProperty(reservationId);
        this.amount = new SimpleDoubleProperty(amount);
        this.date = new SimpleObjectProperty<>(date);
        this.conditionDePaiement = new SimpleStringProperty(conditionDePaiement);
        this.modeDePaiement = new SimpleStringProperty(modeDePaiement);
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

    public String getConditionDePaiement() {
        return conditionDePaiement.get();
    }

    public void setConditionDePaiement(String conditionDePaiement) {
        this.conditionDePaiement.set(conditionDePaiement);
    }

    public StringProperty conditionDePaiementProperty() {
        return conditionDePaiement;
    }

    public String getModeDePaiement() {
        return modeDePaiement.get();
    }

    public void setModeDePaiement(String modeDePaiement) {
        this.modeDePaiement.set(modeDePaiement);
    }

    public StringProperty modeDePaiementProperty() {
        return modeDePaiement;
    }
}
