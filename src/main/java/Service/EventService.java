package Service;
import Entite.Event;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventService {
    private final Connection conn;
    private static final String INSERT_QUERY = "INSERT INTO events (title, description, date, responsable_event_id, hotel_id, capacity) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE events SET title=?, description=?, date=?, responsable_event_id=?, hotel_id=?, capacity=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM events WHERE id=?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM events";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM events WHERE id=?";
    private static final String GET_RESPONSABLE_ID = "SELECT id FROM users WHERE name=?";
    private static final String GET_HOTEL_ID = "SELECT id FROM hotels WHERE name=?";
    private static final String SELECT_BY_RESPONSABLE_ID = "SELECT * FROM events WHERE responsable_event_id=?"; // ✅ New Query
    private static final String GET_RESPONSABLE_NAME_BY_ID = "SELECT name FROM users WHERE id=?";
    private static final String GET_HOTEL_NAME_BY_ID = "SELECT name FROM hotels WHERE id=?";
    private static final String GET_HOTEL_LOCATION_BY_ID = "SELECT location FROM hotels WHERE id=?";
    public EventService(Connection connection) {
        this.conn = connection;
    }

    public void add(Event event) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, event.getTitle());
            pst.setString(2, event.getDescription());
            pst.setDate(3, Date.valueOf(event.getDate()));
            pst.setInt(4, event.getResponsibleEventId());
            pst.setInt(5, event.getHotelId());
            pst.setInt(6, event.getCapacity());
            pst.executeUpdate();

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    event.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void update(Event event) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(UPDATE_QUERY)) {
            pst.setString(1, event.getTitle());
            pst.setString(2, event.getDescription());
            pst.setDate(3, Date.valueOf(event.getDate()));
            pst.setInt(4, event.getResponsibleEventId());
            pst.setInt(5, event.getHotelId());
            pst.setInt(6, event.getCapacity());
            pst.setInt(7, event.getId());
            pst.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(DELETE_QUERY)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    public List<Event> getAll() throws SQLException {
        if (conn.isClosed()) {
            throw new SQLException("Database connection is CLOSED!"); // ✅ Debugging log
        }

        List<Event> events = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL_QUERY)) {

            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("capacity"),
                        rs.getInt("hotel_id"),
                        rs.getInt("responsable_event_id")
                ));
            }
        }
        return events;
    }

    public Event getById(int id) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(SELECT_BY_ID_QUERY)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Event(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getDate("date").toLocalDate(),
                            rs.getInt("responsable_event_id"),
                            rs.getInt("hotel_id"),
                            rs.getInt("capacity")
                    );
                }
            }
        }
        return null;
    }

    public int getResponsableIdByName(String name) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(GET_RESPONSABLE_ID)) {
            pst.setString(1, name);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1;
    }

    public int getHotelIdByName(String name) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(GET_HOTEL_ID)) {
            pst.setString(1, name);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1;
    }
    public List<Event> getByResponsableId(int responsableId) throws SQLException {
        List<Event> events = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(SELECT_BY_RESPONSABLE_ID)) {
            pst.setInt(1, responsableId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    events.add(new Event(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getDate("date").toLocalDate(),
                            rs.getInt("capacity"),
                            rs.getInt("hotel_id"),
                            rs.getInt("responsable_event_id")
                    ));
                }
            }
        }
        return events;
    }
    public String getResponsableNameById(int responsableId) {
        try (PreparedStatement pst = conn.prepareStatement(GET_RESPONSABLE_NAME_BY_ID)) {
            pst.setInt(1, responsableId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching responsable name: " + e.getMessage());
        }
        return "Unknown"; // Return "Unknown" if no responsable found
    }
    public String getHotelNameById(int hotelId) {
        try (PreparedStatement pst = conn.prepareStatement(GET_HOTEL_NAME_BY_ID)) {
            pst.setInt(1, hotelId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching hotel name: " + e.getMessage());
        }
        return "Unknown"; // Return "Unknown" if no hotel found
    }public String getHotelLocationById(int hotelId) {
        try (PreparedStatement pst = conn.prepareStatement(GET_HOTEL_LOCATION_BY_ID)) {
            pst.setInt(1, hotelId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("location");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching hotel location: " + e.getMessage());
        }
        return "Unknown"; // Return "Unknown" if no hotel found
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
        return "Unknown"; // Return a default value if the event is not found
    }
}
