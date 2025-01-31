package Services;

import Entites.Enum.Role;
import Entites.Users;
import Utils.DataSource;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoLogin {
    public static Users login(String email, String password) {
        String query = "SELECT id, name, email, password, role FROM users WHERE email = ? AND password = ?";

        try (Connection cn = DataSource.getInstance().getConn();
             PreparedStatement pst = cn.prepareStatement(query)) {
            pst.setString(1, email);
            pst.setString(2, password);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Users(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            Role.valueOf(rs.getString("role"))
                    );
                }
            }
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while accessing the database: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        return null;
    }

    private static void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.show();
    }

}