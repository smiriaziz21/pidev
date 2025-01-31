package Service;

import Entite.Event;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventService {

    public void addEvent(Event event) {
        String query = "INSERT INTO events (title, description, date, responsable_event_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DataSource.getInstance().getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getDescription());
            stmt.setDate(3, new java.sql.Date(event.getDate().getTime()));
            stmt.setInt(4, event.getResponsableEventId());
            stmt.executeUpdate();
            System.out.println("Event added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM events";
        try (Connection conn = DataSource.getInstance().getCon();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("date"),
                        rs.getInt("responsable_event_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public void updateEvent(Event event) {
        String query = "UPDATE events SET title = ?, description = ?, date = ?, responsable_event_id = ? WHERE id = ?";
        try (Connection conn = DataSource.getInstance().getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getDescription());
            stmt.setDate(3, new java.sql.Date(event.getDate().getTime()));
            stmt.setInt(4, event.getResponsableEventId());
            stmt.setInt(5, event.getId());
            stmt.executeUpdate();
            System.out.println("Event updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEvent(int eventId) {
        String query = "DELETE FROM events WHERE id = ?";
        try (Connection conn = DataSource.getInstance().getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, eventId);
            stmt.executeUpdate();
            System.out.println("Event deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
