package Entite;

import java.time.LocalDate;

public class Facture {

    private int id;
    private int reservationId;
    private double amount;
    private LocalDate date;

    public Facture(int reservationId, double amount, LocalDate date) {
        this.reservationId = reservationId;
        this.amount = amount;
        this.date = date;
    }

    public Facture(int id, int reservationId, double amount, LocalDate date) {
        this.id = id;
        this.reservationId = reservationId;
        this.amount = amount;
        this.date = date;
    }

    public Facture() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
