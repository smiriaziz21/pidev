package Service;

import Entite.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private Connection conn;
    private static final String INSERT_QUERY = "INSERT INTO reservations (client_id, event_id, status) VALUES (?, ?, ?)";
    private static final String UPDATE_STATUS_QUERY = "UPDATE reservations SET status=? WHERE id=?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM reservations";
    private static final String SELECT_BY_CLIENT_QUERY = "SELECT * FROM reservations WHERE client_id=?";

    public ReservationService(Connection connection) {
        this.conn = connection;
    }

    public void addReservation(Reservation reservation) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(INSERT_QUERY)) {
            pst.setInt(1, reservation.getClientId());
            pst.setInt(2, reservation.getEventId());
            pst.setString(3, reservation.getStatus());
            pst.executeUpdate();
        }
    }

    public void updateReservationStatus(int id, String status) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(UPDATE_STATUS_QUERY)) {
            pst.setString(1, status);
            pst.setInt(2, id);
            pst.executeUpdate();
        }
    }

    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL_QUERY)) {
            while (rs.next()) {
                reservations.add(new Reservation(
                        rs.getInt("id"),
                        rs.getInt("client_id"),
                        rs.getInt("event_id"),
                        rs.getString("status")
                ));
            }
        }
        return reservations;
    }

    public List<Reservation> getClientReservations(int clientId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(SELECT_BY_CLIENT_QUERY)) {
            pst.setInt(1, clientId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    reservations.add(new Reservation(
                            rs.getInt("id"),
                            rs.getInt("client_id"),
                            rs.getInt("event_id"),
                            rs.getString("status")
                    ));
                }
            }
        }
        return reservations;
    }
    public List<Reservation> getReservationsByResponsable(int responsableId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT r.* FROM reservations r " +
                "JOIN events e ON r.event_id = e.id " +
                "WHERE e.responsable_event_id = ?"; // Filter by responsable ID
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, responsableId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    reservations.add(new Reservation(
                            rs.getInt("id"),
                            rs.getInt("client_id"),
                            rs.getInt("event_id"),
                            rs.getString("status")
                    ));
                }
            }
        }
        return reservations;
    }
    public String getClientNameById(int clientId) throws SQLException {
        String query = "SELECT name FROM users WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, clientId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        }
        return "Unknown";
    }

    public String getEventTitleById(int eventId) throws SQLException {
        String query = "SELECT title FROM events WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, eventId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("title");
                }
            }
        }
        return "Unknown";
    }
}