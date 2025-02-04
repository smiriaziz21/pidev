package Service;

import Entite.Facture;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFacture implements IService<Facture> {

    private Connection con = DataSource.getInstance().getCon();

    @Override
    public void ajouter(Facture facture) throws SQLException {
        String req = "INSERT INTO invoices (reservation_id, amount, date) VALUES (?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(req)) {
            pst.setInt(1, facture.getReservationId());
            pst.setDouble(2, facture.getAmount());
            pst.setDate(3, Date.valueOf(facture.getDate()));
            pst.executeUpdate();
        }
    }

    @Override
    public void supprimer(Facture facture) throws SQLException {
        String req = "DELETE FROM invoices WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(req)) {
            pst.setInt(1, facture.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public void update(Facture facture) throws SQLException {
        String req = "UPDATE invoices SET reservation_id = ?, amount = ?, date = ? WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(req)) {
            pst.setInt(1, facture.getReservationId());
            pst.setDouble(2, facture.getAmount());
            pst.setDate(3, Date.valueOf(facture.getDate()));
            pst.setInt(4, facture.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public Facture findById(int id) throws SQLException {
        String req = "SELECT * FROM invoices WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(req)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Facture(
                        rs.getInt("id"),
                        rs.getInt("reservation_id"),
                        rs.getDouble("amount"),
                        rs.getDate("date").toLocalDate()
                );
            }
        }
        return null;
    }

    @Override
    public List<Facture> getAll() throws SQLException {
        List<Facture> facturesList = new ArrayList<>();
        String req = "SELECT * FROM invoices";
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Facture facture = new Facture(
                        rs.getInt("id"),
                        rs.getInt("reservation_id"),
                        rs.getDouble("amount"),
                        rs.getDate("date").toLocalDate()
                );
                facturesList.add(facture);
            }
        }
        return facturesList;
    }
}
