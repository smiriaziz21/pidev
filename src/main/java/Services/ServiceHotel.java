package Services;

import Entites.Hotel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Utils.DataSource;

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
        String req = "INSERT INTO `hotels` ( `name`, `location`, `responsable_hotel_id`) VALUES ( '"
                     + hotel.getName() + "', '" 
                     + hotel.getLocation() + "', '" 
                     + hotel.getResponsableHotelId() + "');";
        st.executeUpdate(req);
    }



    @Override
    public void supprimer(Hotel hotel) throws SQLException {
        String req = "DELETE FROM `hotels` WHERE `id` = " + hotel.getId() + ";";
        st.executeUpdate(req);
    }


    @Override
    public void update(Hotel hotel) throws SQLException {
        String req = "UPDATE `hotels` SET `name` = '" + hotel.getName() +
                "', `location` = '" + hotel.getLocation() +
                "', `responsable_hotel_id` = " + hotel.getResponsableHotelId() +
                " WHERE `id` = " + hotel.getId() + ";";
        st.executeUpdate(req);
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
            int responsableHotelId = rs.getInt("responsable_hotel_id");
            Hotel hotel = new Hotel(id, name, location, responsableHotelId);
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
            int responsableHotelId = rs.getInt("responsable_hotel_id");
            Hotel hotel = new Hotel(id, name, location, responsableHotelId);
            list.add(hotel);
        }
        return list;
    }
}
