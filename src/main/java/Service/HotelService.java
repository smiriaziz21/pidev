package Service;


import Entite.Hotels;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelService {
    private Connection conn;

    public HotelService(Connection connection) {
        this.conn = connection;
    }

    public List<Hotels> getAll() throws SQLException {
        List<Hotels> hotels = new ArrayList<>();
        String query = "SELECT * FROM hotels"; // ✅ Added etoiles column

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                hotels.add(new Hotels(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getInt("responsable_hotel_id"),
                        rs.getInt("etoiles") // ✅ Fetch etoiles
                ));
            }
        }
        return hotels;
    }
    public Hotels getById(int id) throws SQLException {
        String query = "SELECT * FROM hotels WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Hotels(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("location"),
                            rs.getInt("responsable_hotel_id"),
                            rs.getInt("etoiles") // ✅ Fetch etoiles
                    );
                }
            }
        }
        return null; // Hotel not found
    }

}