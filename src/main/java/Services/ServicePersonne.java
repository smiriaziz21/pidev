package Services;

import Entites.Users;
import Entites.Enum.Role;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePersonne {

    private static ServicePersonne instance;
    private final Connection con;

    public ServicePersonne() {
        con = DataSource.getInstance().getConn();
    }

    public static ServicePersonne getInstance() {
        if (instance == null) {
            instance = new ServicePersonne();
        }
        return instance;
    }

    public boolean ajouter(Users user) throws SQLException {
        String query = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole().name());
            return pstmt.executeUpdate() > 0;
        }
    }

    public void supprimer(int userId) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }

    public void update(Users user) throws SQLException {
        String query = "UPDATE users SET name = ?, email = ?, password = ?, role = ? WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole().name());
            pstmt.setInt(5, user.getId());
            pstmt.executeUpdate();
        }
    }

    public Users getById(int id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Users(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        Role.valueOf(rs.getString("role"))
                );
            }
            return null;
        }
    }

    public List<Users> getAll() throws SQLException {
        List<Users> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                users.add(new Users(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        Role.valueOf(rs.getString("role"))
                ));
            }
        }
        return users;
    }
}
