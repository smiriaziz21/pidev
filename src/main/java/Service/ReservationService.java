package Service;

import Entite.Reservation;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {

    public void addReservation(Reservation reservation) {
        String query = "INSERT INTO reservations (client_id, event_id, status) VALUES (?, ?, ?)";
        try (Connection conn = DataSource.getInstance().getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, reservation.getClientId());
            stmt.setInt(2, reservation.getEventId());
            stmt.setString(3, reservation.getStatus());
            stmt.executeUpdate();
            System.out.println("Reservation added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateReservationStatus(int reservationId, String status) {
        String query = "UPDATE reservations SET status = ? WHERE id = ?";
        try (Connection conn = DataSource.getInstance().getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, reservationId);
            stmt.executeUpdate();
            System.out.println("Reservation updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteReservation(int reservationId) {
        String query = "DELETE FROM reservations WHERE id = ?";
        try (Connection conn = DataSource.getInstance().getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, reservationId);
            stmt.executeUpdate();
            System.out.println("Reservation deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
