package Controllers;

import Entites.Users;
import Services.ServicePersonne;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterPersonneController {

    @FXML
    private TextField txtage;

    @FXML
    private TextField txtnom;

    @FXML
    private TextField txtprenom;

//    @FXML
//    void ajouter(ActionEvent event) {
//
//
//        Users p1=new Users(txtnom.getText(),txtprenom.getText(),Integer.parseInt(txtage.getText()));
//
//        ServicePersonne ser=new ServicePersonne();
//
//        try {
//            ser.ajouter(p1);
//            System.out.println("personne ajoutée");
//        } catch (SQLException e) {
//            System.out.println(p1);
//        }
//    }

    @FXML
    void afficher(ActionEvent event) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/AfficherPersonne.fxml"));

        Parent root=loader.load();
        txtage.getScene().setRoot(root);
    }

}