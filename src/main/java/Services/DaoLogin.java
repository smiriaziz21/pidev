package Services;

import Entites.Enum.Role;
import Entites.Users;
import Utils.DataSource;
import Utils.PasswordGenerator;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.mindrot.jbcrypt.BCrypt;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DaoLogin {

    // User login method
    public static Users login(String email, String password) {
        String query = "SELECT id, name, email, password, role FROM users WHERE email = ?";

        try (Connection cn = DataSource.getInstance().getConn();
             PreparedStatement pst = cn.prepareStatement(query)) {
            pst.setString(1, email);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");

                    if (BCrypt.checkpw(password, hashedPassword)) {
                        return new Users(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                hashedPassword,
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

    // Reset password and send a new random one
    public static String resetPasswordWithRandom(String email) {
        String randomPassword = PasswordGenerator.generateRandomPassword(12);
        String hashedPassword = BCrypt.hashpw(randomPassword, BCrypt.gensalt());

        String query = "UPDATE users SET password = ? WHERE email = ?";

        try (Connection cn = DataSource.getInstance().getConn();
             PreparedStatement pst = cn.prepareStatement(query)) {
            pst.setString(1, hashedPassword);
            pst.setString(2, email);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                sendPasswordEmail(email, randomPassword);
                return randomPassword;
            } else {
                showAlert("Password Reset Failed", "No user found with this email.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to reset password: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        return null;
    }

    // Send an email with the new password
    private static void sendPasswordEmail(String recipientEmail, String randomPassword) {
        String host = "smtp.gmail.com";
        String port = "587";
        String senderEmail = "laytharfaoui48@gmail.com";
        String senderPassword = "xsrs nqpr hyci pwpx"; // Store securely, e.g., in environment variables

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject("🔐 Password Reset Notification");

            // Formatted email message
            String emailContent = "Dear User,\n\n"
                    + "Your password has been successfully reset. Please find your new login credentials below:\n\n"
                    + "🔑 **New Password:** " + randomPassword + "\n\n"
                    + "Please log in and change your password as soon as possible for security reasons.\n\n"
                    + "Best regards,\n"
                    + "Your App Team";

            message.setText(emailContent);
            Transport.send(message);

            System.out.println("Password reset email sent successfully to " + recipientEmail);
        } catch (MessagingException e) {
            System.err.println("Failed to send password reset email: " + e.getMessage());
        }
    }

    // Block a user by username or email
    public static boolean blockUserByUsername(String username) {
        String query = "UPDATE users SET status = 'BLOCKED' WHERE email = ? OR name = ?";

        try (Connection cn = DataSource.getInstance().getConn();
             PreparedStatement pst = cn.prepareStatement(query)) {
            pst.setString(1, username); // Match email
            pst.setString(2, username); // Match username
            int rowsUpdated = pst.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Failed to block user: " + e.getMessage());
            return false;
        }
    }

    // Unblock a user by username or email
    public static boolean unblockUserByUsername(String username) {
        String query = "UPDATE users SET status = 'ACTIVE' WHERE email = ? OR name = ?";

        try (Connection cn = DataSource.getInstance().getConn();
             PreparedStatement pst = cn.prepareStatement(query)) {
            pst.setString(1, username); // Match email
            pst.setString(2, username); // Match username
            int rowsUpdated = pst.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Failed to unblock user: " + e.getMessage());
            return false;
        }
    }
    // Utility method to show alerts
    private static void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.show();
    }
}