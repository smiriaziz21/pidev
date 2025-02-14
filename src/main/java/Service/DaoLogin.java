package Service;

import Entite.Enum.Role;
import Entite.Users;
import Utils.DataSource;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoLogin {

    public static Users login(String email, String password) {
        String query = "SELECT id, name, email, password, role FROM users WHERE email = ?";

        try (Connection cn = DataSource.getInstance().getCon();
             PreparedStatement pst = cn.prepareStatement(query)) {
            pst.setString(1, email);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");

                    // Verify the provided password against the hashed password
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        return new Users(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                hashedPassword, // Return the hashed password (not the plain one)
                                Role.valueOf(rs.getString("role"))
                        );
                    } else {
                        showAlert("Login Failed", "Invalid email or password.", Alert.AlertType.ERROR);
                    }
                } else {
                    showAlert("Login Failed", "User not found.", Alert.AlertType.ERROR);
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