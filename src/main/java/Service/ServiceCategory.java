package Service;

import Entite.Categories;
import Utils.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategory {
    private Connection con = DataSource.getInstance().getCon();

    public void ajouter(Categories category) throws SQLException {
        String req = "INSERT INTO categories (name, description) VALUES (?, ?)";
        try (PreparedStatement pst = con.prepareStatement(req)) {
            pst.setString(1, category.getName());
            pst.setString(2, category.getDescription());
            pst.executeUpdate();
        }
    }

    public void supprimer(int categoryId) throws SQLException {
        String req = "DELETE FROM categories WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(req)) {
            pst.setInt(1, categoryId);
            pst.executeUpdate();
        }
    }
    public Categories findByName(String name) throws SQLException {
        String req = "SELECT * FROM categories WHERE name = ?";
        try (PreparedStatement pst = con.prepareStatement(req)) {
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Categories(rs.getInt("id"), rs.getString("name"), rs.getString("description"));
            }
        }
        return null;
    }
    public void update(Categories category) throws SQLException {
        String query = "UPDATE categories SET name = ?, description = ? WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getId());

            int rowsUpdated = stmt.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);
        }
    }


    public Categories findById(int id) throws SQLException {
        String req = "SELECT * FROM categories WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(req)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Categories(rs.getInt("id"), rs.getString("name"), rs.getString("description"));
            }
        }
        return null;
    }

    public List<Categories> getAll() throws SQLException {
        List<Categories> categories = new ArrayList<>();
        String req = "SELECT * FROM categories";
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                categories.add(new Categories(rs.getInt("id"), rs.getString("name"), rs.getString("description")));
            }
        }
        return categories;
    }
}