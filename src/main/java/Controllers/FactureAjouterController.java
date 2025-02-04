package Controllers;

import Entite.Facture;
import Service.ServiceFacture;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FactureAjouterController {

    @FXML
    private TextField txtReservationId;
    @FXML
    private TextField txtAmount;
    @FXML
    private TextField txtDate;

    private final ServiceFacture service = new ServiceFacture();
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @FXML
    void ajouter(ActionEvent event) {
        try {
            int reservationId = Integer.parseInt(txtReservationId.getText());
            double amount = Double.parseDouble(txtAmount.getText());
            Date date = formatter.parse(txtDate.getText());

            Facture facture = new Facture(reservationId, amount, new java.sql.Date(date.getTime()).toLocalDate());

            service.ajouter(facture);
            System.out.println("Facture added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding facture: " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("Invalid date format: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    @FXML
    void update(ActionEvent event) {
        try {
            int reservationId = Integer.parseInt(txtReservationId.getText());
            double amount = Double.parseDouble(txtAmount.getText());
            Date date = formatter.parse(txtDate.getText());

            Facture facture = new Facture(reservationId, amount, new java.sql.Date(date.getTime()).toLocalDate());

            service.update(facture);
            System.out.println("Facture updated successfully!");

        } catch (SQLException e) {
            System.out.println("Error updating facture: " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("Invalid date format: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    @FXML
    void delete(ActionEvent event) {
        try {
            int reservationId = Integer.parseInt(txtReservationId.getText());

            Facture factureToDelete = new Facture();
            factureToDelete.setReservationId(reservationId);

            service.supprimer(factureToDelete);
            System.out.println("Facture deleted successfully!");

        } catch (SQLException e) {
            System.out.println("Error deleting facture: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }
}
