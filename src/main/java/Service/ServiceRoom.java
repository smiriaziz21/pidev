package Service;

import Entite.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Utils.DataSource;

public  class ServiceRoom {

    private Connection con = DataSource.getInstance().getCon();
    private Statement st;

    public ServiceRoom() {
        try {
            st = con.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void ajouter(Room room) throws SQLException {
        String req = "INSERT INTO `rooms` (`id`, `hotel_id`, `room_number`, `capacity`) VALUES (NULL, '"
                + room.getHotelId() + "', '"
                + room.getRoomNumber() + "', '"
                + room.getCapacity() + "');";
        st.executeUpdate(req);
    }

    public void ajouterPSTM(Room room) throws SQLException {
        PreparedStatement pre = con.prepareStatement("INSERT INTO `rooms` (`hotel_id`, `room_number`, `capacity`) VALUES (?, ?, ?);");
        pre.setInt(1, room.getHotelId());
        pre.setString(2, room.getRoomNumber());
        pre.setInt(3, room.getCapacity());
        pre.executeUpdate();
    }

    public void supprimer(Room room) throws SQLException {
        String req = "DELETE FROM `rooms` WHERE `id` = " + room.getId() + ";";
        st.executeUpdate(req);
    }

    public void update(Room room) throws SQLException {
        String req = "UPDATE `rooms` SET `hotel_id` = '" + room.getHotelId() +
                "', `room_number` = '" + room.getRoomNumber() +
                "', `capacity` = '" + room.getCapacity() +
                "' WHERE `id` = " + room.getId() + ";";
        st.executeUpdate(req);
    }



    public List<Room> getAll() throws SQLException {
        List<Room> list = new ArrayList<>();
        ResultSet rs = st.executeQuery("SELECT * FROM `rooms`;");
        while (rs.next()) {
            int id = rs.getInt("id");
            int hotelId = rs.getInt("hotel_id");
            String roomNumber = rs.getString("room_number");
            int capacity = rs.getInt("capacity");
            Room room = new Room(id, hotelId, roomNumber, capacity);
            list.add(room);
        }
        return list;
    }


    public List<Room> getRoomsByResponsableId( int responsableId,int hotelId) throws SQLException {
        List<Room> list = new ArrayList<>();
        String query = "SELECT r.id, r.hotel_id, r.room_number, r.capacity " +
                "FROM rooms r " +
                "JOIN hotels h ON r.hotel_id = h.id " +
                "WHERE h.responsable_hotel_id = ? AND h.id = ?";

        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setInt(1, responsableId);
        pstmt.setInt(2, hotelId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            int hotel_Id = rs.getInt("hotel_id");
            String roomNumber = rs.getString("room_number");
            int capacity = rs.getInt("capacity");
            Room room = new Room(id, hotel_Id, roomNumber, capacity);
            list.add(room);
        }

        return list;
    }



}