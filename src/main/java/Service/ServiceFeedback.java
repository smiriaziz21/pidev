package Service;

import Entite.Feedback;
import Utils.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFeedback implements IService<Feedback> {

    private Connection con = DataSource.getInstance().getCon();

    @Override
    public void ajouter(Feedback feedback) throws SQLException {
        String req = "INSERT INTO feedback (client_id, event_id, comment, rating, date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(req)) {
            pst.setInt(1, feedback.getClientId());
            pst.setInt(2, feedback.getEventId());
            pst.setString(3, feedback.getComment());
            pst.setInt(4, feedback.getRating());
            pst.setDate(5, Date.valueOf(feedback.getDate()));
            pst.executeUpdate();
        }
    }

    @Override
    public void supprimer(Feedback feedback) throws SQLException {
        String req = "DELETE FROM feedback WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(req)) {
            pst.setInt(1, feedback.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public void update(Feedback feedback) throws SQLException {
        String req = "UPDATE feedback SET client_id = ?, event_id = ?, comment = ?, rating = ?, date = ? WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(req)) {
            pst.setInt(1, feedback.getClientId());
            pst.setInt(2, feedback.getEventId());
            pst.setString(3, feedback.getComment());
            pst.setInt(4, feedback.getRating());
            pst.setDate(5, Date.valueOf(feedback.getDate()));
            pst.setInt(6, feedback.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public Feedback findById(int id) throws SQLException {
        String req = "SELECT * FROM feedback WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(req)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Feedback(
                        rs.getInt("id"),
                        rs.getInt("client_id"),
                        rs.getInt("event_id"),
                        rs.getString("comment"),
                        rs.getInt("rating"),
                        rs.getDate("date").toLocalDate()
                );
            }
        }
        return null;
    }

    @Override
    public List<Feedback> getAll() throws SQLException {
        List<Feedback> feedbackList = new ArrayList<>();
        String req = "SELECT * FROM feedback";
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Feedback feedback = new Feedback(
                        rs.getInt("id"),
                        rs.getInt("client_id"),
                        rs.getInt("event_id"),
                        rs.getString("comment"),
                        rs.getInt("rating"),
                        rs.getDate("date").toLocalDate()
                );
                feedbackList.add(feedback);
            }
        }
        System.out.println("Feedback list size: " + feedbackList.size());
        return feedbackList;
    }

    // Fetch client IDs for the ComboBox
    public List<Integer> getClientIds() throws SQLException {
        List<Integer> clientIds = new ArrayList<>();
        String req = "SELECT DISTINCT client_id FROM feedback";
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                clientIds.add(rs.getInt("client_id"));
            }
        }
        return clientIds;
    }

    // Fetch event IDs for the ComboBox
    public List<Integer> getEventIds() throws SQLException {
        List<Integer> eventIds = new ArrayList<>();
        String req = "SELECT DISTINCT event_id FROM feedback";
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                eventIds.add(rs.getInt("event_id"));
            }
        }
        return eventIds;
    }

    public List<String> getClientIdsAsStrings() throws SQLException {
        // Retrieve client IDs as Strings from the database
        List<String> clientIds = new ArrayList<>();
        String req = "SELECT DISTINCT client_id FROM feedback"; // Fixed query
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(req); // Execute the query
            while (rs.next()) {
                clientIds.add(String.valueOf(rs.getInt("client_id")));
            }
        }
        return clientIds;
    }

    public List<String> getEventIdsAsStrings() throws SQLException {
        // Retrieve event IDs as Strings from the database
        List<String> eventIds = new ArrayList<>();
        String req = "SELECT DISTINCT event_id FROM feedback"; // Fixed query
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(req); // Execute the query
            while (rs.next()) {
                eventIds.add(String.valueOf(rs.getInt("event_id")));
            }
        }
        return eventIds;
    }

}
