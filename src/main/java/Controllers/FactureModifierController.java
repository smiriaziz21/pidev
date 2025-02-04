package Controllers;

import Entite.Facture;
import Service.ServiceFacture;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FactureModifierController {

    @FXML
    private TextField txtReservationId;
    @FXML
    private TextField txtAmount;
    @FXML
    private TextField txtDate;

    private Facture currentFacture;
    private final ServiceFacture service = new ServiceFacture();

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public void initData(Facture facture) {
        this.currentFacture = facture;

        txtReservationId.setText(String.valueOf(facture.getReservationId()));
        txtAmount.setText(String.valueOf(facture.getAmount()));
        txtDate.setText(formatter.format(facture.getDate()));
    }

    @FXML
    private void updateFacture() {
        try {
            currentFacture.setReservationId(Integer.parseInt(txtReservationId.getText()));
            currentFacture.setAmount(Double.parseDouble(txtAmount.getText()));
            Date date = formatter.parse(txtDate.getText());
            currentFacture.setDate(new java.sql.Date(date.getTime()).toLocalDate());

            service.update(currentFacture);

            Stage stage = (Stage) txtReservationId.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goBack(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage) txtReservationId.getScene().getWindow();
        stage.close();
    }
}
