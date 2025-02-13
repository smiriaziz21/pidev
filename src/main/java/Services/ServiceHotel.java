package Services;

import Entites.Hotel;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceHotel implements IService<Hotel> {

    private Connection con = DataSource.getInstance().getConn();
    private Statement st;

    public ServiceHotel() {
        try {
            st = con.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void ajouter(Hotel hotel) throws SQLException {
        String req = "INSERT INTO `hotels` (`name`, `location`, `responsable_hotel_id`, `etoiles`) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(req);
        pstmt.setString(1, hotel.getName());
        pstmt.setString(2, hotel.getLocation());
        pstmt.setInt(3, hotel.getResponsableHotelId());
        pstmt.setInt(4, hotel.getEtoiles()); // Ajout du champ etoiles
        pstmt.executeUpdate();
    }

    @Override
    public void supprimer(Hotel hotel) throws SQLException {
        String req = "DELETE FROM `hotels` WHERE `id` = ?";
        PreparedStatement pstmt = con.prepareStatement(req);
        pstmt.setInt(1, hotel.getId());
        pstmt.executeUpdate();
    }

    @Override
    public void update(Hotel hotel) throws SQLException {
        String req = "UPDATE `hotels` SET `name` = ?, `location` = ?, `responsable_hotel_id` = ?, `etoiles` = ? WHERE `id` = ?";
        PreparedStatement pstmt = con.prepareStatement(req);
        pstmt.setString(1, hotel.getName());
        pstmt.setString(2, hotel.getLocation());
        pstmt.setInt(3, hotel.getResponsableHotelId());
        pstmt.setInt(4, hotel.getEtoiles()); // Mise à jour du champ etoiles
        pstmt.setInt(5, hotel.getId());
        pstmt.executeUpdate();
    }

    public List<String> getAllHotelNamesByResponsableId(int responsableId) throws SQLException {
        List<String> hotelNames = new ArrayList<>();
        String query = "SELECT name FROM hotels WHERE responsable_hotel_id = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setInt(1, responsableId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            hotelNames.add(rs.getString("name"));
        }
        return hotelNames;
    }

    public List<Hotel> getAllByResponsableId(int responsableId) throws SQLException {
        List<Hotel> list = new ArrayList<>();
        String query = "SELECT * FROM hotels WHERE responsable_hotel_id = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setInt(1, responsableId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String location = rs.getString("location");
            int etoiles = rs.getInt("etoiles"); // Récupération du champ etoiles
            int responsableHotelId = rs.getInt("responsable_hotel_id");
            Hotel hotel = new Hotel(id, name, location, responsableHotelId, etoiles);
            list.add(hotel);
        }
        return list;
    }

    @Override
    public List<Hotel> getAll() throws SQLException {
        List<Hotel> list = new ArrayList<>();
        ResultSet rs = st.executeQuery("SELECT * FROM `hotels`;");
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String location = rs.getString("location");
            int etoiles = rs.getInt("etoiles"); // Récupération du champ etoiles
            int responsableHotelId = rs.getInt("responsable_hotel_id");
            Hotel hotel = new Hotel(id, name, location, responsableHotelId, etoiles);
            list.add(hotel);
        }
        return list;
    }
}
