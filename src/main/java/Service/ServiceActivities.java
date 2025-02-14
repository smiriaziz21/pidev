package Service;

import Entite.Activities;
import Utils.DataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceActivities implements IService<Activities> {

    private Connection con = DataSource.getInstance().getCon();

    public ServiceActivities() {}

    @Override
    public void ajouter(Activities activities) throws SQLException {
        String req = "INSERT INTO activities (id_event, name, description, start_date, end_date, location, responsible_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement pst = con.prepareStatement(req)) {
            pst.setInt(1, activities.getIdEvent());
            pst.setString(2, activities.getName());
            pst.setString(3, activities.getDescription());
            pst.setTimestamp(4, Timestamp.valueOf(activities.getStartDate()));
            pst.setTimestamp(5, Timestamp.valueOf(activities.getEndDate()));
            pst.setString(6, activities.getLocation());
            if (activities.getResponsibleId() == null) {
                pst.setNull(7, Types.INTEGER);
            } else {
                pst.setInt(7, activities.getResponsibleId());
            }

            pst.executeUpdate();
            System.out.println("Activity added successfully!");
        }
    }

    @Override
    public void supprimer(Activities activities) throws SQLException {

    }
    public void delete(int activityId) throws SQLException {
        String query = "DELETE FROM activities WHERE id_activity = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, activityId);
            pst.executeUpdate();
        }
    }



    @Override
    public void update(Activities activities) throws SQLException {
        String req = "UPDATE activities SET name = ?, description = ?, start_date = ?, end_date = ?, location = ?, responsible_id = ? " +
                "WHERE id_activity = ?;";
        try (PreparedStatement pst = con.prepareStatement(req)) {
            pst.setString(1, activities.getName());
            pst.setString(2, activities.getDescription());
            pst.setTimestamp(3, Timestamp.valueOf(activities.getStartDate()));
            pst.setTimestamp(4, Timestamp.valueOf(activities.getEndDate()));
            pst.setString(5, activities.getLocation());
            if (activities.getResponsibleId() == null) {
                pst.setNull(6, Types.INTEGER);
            } else {
                pst.setInt(6, activities.getResponsibleId());
            }
            pst.setInt(7, activities.getId());
            pst.executeUpdate();
            System.out.println("Activity updated successfully!");
        }
    }

    @Override
    public Activities findById(int id) throws SQLException {
        String req = "SELECT * FROM activities WHERE id_activity = ?;";
        try (PreparedStatement pst = con.prepareStatement(req)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Activities(
                        rs.getInt("id_activity"),
                        rs.getInt("id_event"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getTimestamp("start_date").toLocalDateTime(),
                        rs.getTimestamp("end_date").toLocalDateTime(),
                        rs.getString("location"),
                        rs.getInt("responsible_id")
                );
            }
        }
        return null;
    }

    @Override
    public List<Activities> getAll() throws SQLException {
        List<Activities> activitiesList = new ArrayList<>();
        String req = "SELECT * FROM activities;";
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Activities activity = new Activities(
                        rs.getInt("id_activity"),
                        rs.getInt("id_event"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getTimestamp("start_date").toLocalDateTime(),
                        rs.getTimestamp("end_date").toLocalDateTime(),
                        rs.getString("location"),
                        rs.getInt("responsible_id")
                );
                activitiesList.add(activity);
            }
        }
        return activitiesList;
    }

}
