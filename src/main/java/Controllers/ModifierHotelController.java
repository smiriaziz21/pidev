package Controllers;

import Entites.Hotel;
import Services.ServiceHotel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.List;

public class ModifierHotelController {

    @FXML
    private TableColumn<Hotel, Integer> colId;

    @FXML
    private TableColumn<Hotel, String> colName;

    @FXML
    private TableColumn<Hotel, String> colLocation;

    @FXML
    private TableColumn<Hotel, Void> colAction;

    @FXML
    private TableView<Hotel> tableView;

    private ServiceHotel serviceHotel = new ServiceHotel();
    private int currentResponsableId;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupActionColumn();
    }

    public void setResponsableId(int responsableId) {
        this.currentResponsableId = responsableId;
        loadHotels();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
    }

    private void setupActionColumn() {
        colAction.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Hotel, Void> call(final TableColumn<Hotel, Void> param) {
                return new TableCell<>() {
                    private final Button modifyBtn = new Button("Modifier");

                    {
                        modifyBtn.setOnAction(event -> {
                            Hotel hotel = getTableView().getItems().get(getIndex());
                            handleModifyHotel(hotel);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(modifyBtn);
                        }
                    }
                };
            }
        });
    }

    private void loadHotels() {
        try {
            List<Hotel> hotels = serviceHotel.getAllByResponsableId(currentResponsableId);
            ObservableList<Hotel> observableList = FXCollections.observableArrayList(hotels);
            tableView.setItems(observableList);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur de chargement",
                    e.getMessage(), AlertType.ERROR);
        }
    }

    private void handleModifyHotel(Hotel hotel) {
        // Création de la fenêtre de modification
        TextField nameField = new TextField(hotel.getName());
        TextField locationField = new TextField(hotel.getLocation());

        Alert editDialog = new Alert(AlertType.CONFIRMATION);
        editDialog.setTitle("Modifier Hôtel");
        editDialog.setHeaderText("Modification de l'hôtel : " );
        editDialog.getDialogPane().setContent(new javafx.scene.layout.VBox(5,
                new javafx.scene.control.Label("Nom:"), nameField,
                new javafx.scene.control.Label("Localisation:"), locationField
        ));

        editDialog.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    hotel.setName(nameField.getText());
                    hotel.setLocation(locationField.getText());
                    serviceHotel.update(hotel);
                    loadHotels(); // Rafraîchir les données
                    showAlert("Succès", "Modification réussie",
                            "Hôtel mis à jour avec succès", AlertType.INFORMATION);
                } catch (SQLException e) {
                    showAlert("Erreur", "Échec de modification",
                            e.getMessage(), AlertType.ERROR);
                }
            }
        });
    }

    private void showAlert(String title, String header, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }



}