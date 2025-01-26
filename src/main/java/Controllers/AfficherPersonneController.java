package Controllers;
import Entites.Personne;
import Services.ServicePersonne;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class AfficherPersonneController {

    @FXML
    private TableColumn<Personne, Integer> colage;

    @FXML
    private TableColumn<Personne, Integer> colid;

    @FXML
    private TableColumn<Personne, String> colnom;

    @FXML
    private TableColumn<Personne, String> colprenom;

    @FXML
    private TableView<Personne> tableview;


    @FXML
    void initialize() {
        ServicePersonne ser=new ServicePersonne();

        try {
            List<Personne> l1=ser.getAll();
            ObservableList<Personne> obse= FXCollections.observableList(l1);
            tableview.setItems(obse);
            colage.setCellValueFactory(new PropertyValueFactory<>("age"));
            colid.setCellValueFactory(new PropertyValueFactory<>("id"));
            colnom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            colprenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        } catch (SQLException e) {
            System.out.println(e);
        }

    }
}
