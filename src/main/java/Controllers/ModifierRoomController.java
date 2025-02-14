package Controllers;

import Entite.Room;
import Service.ServiceRoom;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.List;

public class ModifierRoomController {

    @FXML
    private TableView<Room> tableView;

    @FXML
    private TableColumn<Room, Integer> colHotelId;

    @FXML
    private TableColumn<Room, String> colRoomNumber;

    @FXML
    private TableColumn<Room, Integer> colCapacity;

    @FXML
    private TableColumn<Room, Void> colAction;

    private ServiceRoom serviceRoom = new ServiceRoom();
    private int currentResponsableId = 1;
    private int idhotel;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupActionColumn();
    }

    public void setHotelId(int hotelId) {
        this.idhotel = hotelId;
        System.out.println("ID de l'hôtel reçu : " + hotelId);
        loadRooms(); // Charger les chambres pour l'hôtel sélectionné
    }

    private void setupTableColumns() {

        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
    }

    private void setupActionColumn() {
        colAction.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Room, Void> call(final TableColumn<Room, Void> param) {
                return new TableCell<>() {
                    private final Button modifyBtn = new Button("Modifier");

                    {
                        modifyBtn.setOnAction(event -> {
                            Room room = getTableView().getItems().get(getIndex());
                            handleModifyRoom(room);
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

    private void loadRooms() {
        try {
            List<Room> rooms = serviceRoom.getRoomsByResponsableId(currentResponsableId, idhotel);
            ObservableList<Room> observableList = FXCollections.observableArrayList(rooms);
            tableView.setItems(observableList);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur de chargement", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void handleModifyRoom(Room room) {
        TextField roomNumberField = new TextField(room.getRoomNumber());
        Spinner<Integer> capacitySpinner = new Spinner<>(1, 10, room.getCapacity());

        Alert editDialog = new Alert(Alert.AlertType.CONFIRMATION);
        editDialog.setTitle("Modifier Chambre");
        editDialog.setHeaderText("Modification de la chambre : ");
        editDialog.getDialogPane().setContent(new javafx.scene.layout.VBox(5,
                new javafx.scene.control.Label("Numéro de chambre:"), roomNumberField,
                new javafx.scene.control.Label("Capacité:"), capacitySpinner
        ));

        editDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    room.setRoomNumber(roomNumberField.getText());
                    room.setCapacity(capacitySpinner.getValue());
                    serviceRoom.update(room);
                    loadRooms(); // Rafraîchir les données
                    showAlert("Succès", "Modification réussie", "Chambre mise à jour avec succès", Alert.AlertType.INFORMATION);
                } catch (SQLException e) {
                    showAlert("Erreur", "Échec de modification", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}