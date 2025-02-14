package Controllers;

import Entite.Hotels;
import Service.ServiceHotel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ModifierHotelController {

    @FXML
    private TableColumn<Hotels, Integer> colId;

    @FXML
    private TableColumn<Hotels, String> colName;

    @FXML
    private TableColumn<Hotels, String> colLocation;

    @FXML
    private TableColumn<Hotels, Integer> colStars; // Ajout de la colonne étoiles

    @FXML
    private TableColumn<Hotels, Void> colAction;

    @FXML
    private TableColumn<Hotels, Void> colModifyRoom;

    @FXML
    private TableView<Hotels> tableView;

    @FXML
    private TextField searchField; // Barre de recherche

    private ServiceHotel serviceHotel = new ServiceHotel();
    private int currentResponsableId;


    private ObservableList<Hotels> hotelsList = FXCollections.observableArrayList(); // Liste complète des hôtels

    @FXML
    public void initialize() {
        setupTableColumns();
        setupActionColumn();
        setupModifyRoomColumn();
        setupSearchFilter(); // Initialiser le filtre de recherche
    }

    public void setResponsableId(int responsableId) {
        this.currentResponsableId = responsableId;
        loadHotels();
    }

    private void setupTableColumns() {

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colStars.setCellValueFactory(new PropertyValueFactory<>("etoiles")); // Utilisation de getEtoiles()
    }

    private void setupActionColumn() {
        colAction.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Hotels, Void> call(final TableColumn<Hotels, Void> param) {
                return new TableCell<>() {
                    private final Button modifyBtn = new Button("Modifier");

                    {
                        modifyBtn.setOnAction(event -> {
                            Hotels hotel = getTableView().getItems().get(getIndex());
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

    private void setupModifyRoomColumn() {
        colModifyRoom.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Hotels, Void> call(final TableColumn<Hotels, Void> param) {
                return new TableCell<>() {
                    private final Button modifyRoomBtn = new Button("Modifier Chambre");

                    {
                        modifyRoomBtn.setOnAction(event -> {
                            Hotels hotel = getTableView().getItems().get(getIndex());
                            openModifierChambre(hotel.getId());
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(modifyRoomBtn);
                        }
                    }
                };
            }
        });
    }

    private void loadHotels() {
        try {
            List<Hotels> hotels = serviceHotel.getAllByResponsableId(currentResponsableId);
            hotelsList.setAll(hotels); // Charger tous les hôtels dans la liste
            tableView.setItems(hotelsList);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur de chargement", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void handleModifyHotel(Hotels hotel) {
        TextField nameField = new TextField(hotel.getName());
        TextField locationField = new TextField(hotel.getLocation());

        // ComboBox pour les étoiles
        ComboBox<Integer> starsComboBox = new ComboBox<>();
        starsComboBox.getItems().addAll(1, 2, 3, 4, 5);
        starsComboBox.setValue(hotel.getEtoiles()); // Utilisation de getEtoiles()

        Alert editDialog = new Alert(Alert.AlertType.CONFIRMATION);
        editDialog.setTitle("Modifier Hôtel");
        editDialog.setHeaderText("Modification de l'hôtel : " + hotel.getName());
        editDialog.getDialogPane().setContent(new VBox(5,
                new Label("Nom :"), nameField,
                new Label("Localisation :"), locationField,
                new Label("Nombre d'étoiles :"), starsComboBox
        ));

        editDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    hotel.setName(nameField.getText());
                    hotel.setLocation(locationField.getText());
                    hotel.setEtoiles(starsComboBox.getValue()); // Utilisation de setEtoiles()
                    serviceHotel.update(hotel);
                    loadHotels();
                    showAlert("Succès", "Modification réussie", "Hôtel mis à jour avec succès", Alert.AlertType.INFORMATION);
                } catch (SQLException e) {
                    showAlert("Erreur", "Échec de modification", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void openModifierChambre(int hotelId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierchambre.fxml"));
            Parent root = loader.load();

            // Passer l'ID de l'hôtel à ModifierChambreController
            ModifierRoomController controller = loader.getController();
            controller.setHotelId(hotelId);

            Stage stage = new Stage();
            stage.setTitle("Modifier Chambres");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterHotels(newValue); // Filtrer lors du changement de texte
        });
    }

    private void filterHotels(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            tableView.setItems(hotelsList); // Réinitialiser à la liste complète
            return;
        }

        String lowerCaseKeyword = keyword.toLowerCase();
        ObservableList<Hotels> filteredList = hotelsList.filtered(hotel ->
                hotel.getName().toLowerCase().contains(lowerCaseKeyword) ||
                        hotel.getLocation().toLowerCase().contains(lowerCaseKeyword) ||
                        String.valueOf(hotel.getEtoiles()).contains(lowerCaseKeyword) // Utilisation de getEtoiles()
        );

        tableView.setItems(filteredList); // Afficher la liste filtrée
    }
}