package Controllers;

import Services.DaoLogin;
import Entites.Users;
import Utils.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import java.sql.*;

public class UserManagementController {

    @FXML
    private TableView<Users> userTable;
    @FXML
    private TableColumn<Users, String> nameColumn;
    @FXML
    private TableColumn<Users, String> emailColumn;
    @FXML
    private TableColumn<Users, String> statusColumn;
    @FXML
    private Button blockButton;
    @FXML
    private Button unblockButton;

    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        loadUsers();
    }

    private void loadUsers() {
        ObservableList<Users> usersList = FXCollections.observableArrayList();
        String query = "SELECT id, name, email, status FROM users";

        try (Connection cn = DataSource.getInstance().getConn();
             PreparedStatement pst = cn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                usersList.add(new Users(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("status"),
                        Entites.Enum.Role.valueOf(rs.getString("role")) // Convert role from DB
                ));
            }
            userTable.setItems(usersList);
        } catch (SQLException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

//    @FXML
//    private void handleBlockUser() {
//        Users selectedUser = userTable.getSelectionModel().getSelectedItem();
//        if (selectedUser != null && DaoLogin.blockUser(selectedUser.getId())) {
//            showAlert("Success", "User has been blocked.", Alert.AlertType.INFORMATION);
//            loadUsers();
//        }
//    }

//    @FXML
//    private void handleUnblockUser() {
//        Users selectedUser = userTable.getSelectionModel().getSelectedItem();
//        if (selectedUser != null && DaoLogin.unblockUser(selectedUser.getId())) {
//            showAlert("Success", "User has been unblocked.", Alert.AlertType.INFORMATION);
//            loadUsers();
//        }
//    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
