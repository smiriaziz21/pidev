package Controllers;

import Entites.Hotel;
import Services.ServiceHotel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class SuprimerHotelController {

    @FXML
    private TableView<Hotel> hotelTable;

    @FXML
    private TableColumn<Hotel, Integer> colId;

    @FXML
    private TableColumn<Hotel, String> colName;

    @FXML
    private TableColumn<Hotel, String> colLocation;

  //  @FXML
  //  private TableColumn<Hotel, Integer> colResponsable;

    @FXML
    private Button deleteButton;

    private ServiceHotel serviceHotel;

    private ObservableList<Hotel> hotelList;

    // Constructeur corrigé
    public SuprimerHotelController() {
        serviceHotel = new ServiceHotel();
    }

    @FXML
    public void initialize() {
        setupTable();
        loadHotels();
    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
       // colResponsable.setCellValueFactory(new PropertyValueFactory<>("responsableHotelId"));

        hotelTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            deleteButton.setDisable(newSelection == null);
        });
    }
int id=1 ;

    private void loadHotels() {
        try {
            hotelList = FXCollections.observableArrayList(serviceHotel.getAllByResponsableId(id));
            hotelTable.setItems(hotelList);
        } catch (SQLException e) {
            showAlert("Error", "Could not load hotels: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void deleteHotel() {
        Hotel selectedHotel = hotelTable.getSelectionModel().getSelectedItem();
        if (selectedHotel == null) {
            showAlert("Warning", "No hotel selected for deletion!", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Assurez-vous que la suppression dans la base de données est réussie
            serviceHotel.supprimer(selectedHotel);
            hotelList.remove(selectedHotel);  // Retirer l'hôtel de la liste observable
            showAlert("Success", "Hotel deleted successfully!", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Error", "Could not delete hotel: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
