package Service;

import Entite.Facture;
import Utils.DataSource;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.exception.StripeException;
import com.stripe.param.PaymentIntentCreateParams;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFacture implements IService<Facture> {

    private Connection con = DataSource.getInstance().getCon();

    private static final String STRIPE_API_KEY = "sk_test_51H2plbD8XywEs7fGHhgkLhxS4asGuw7kAmk2J5h7Ym6gk4zhtHhHzNiKz9DsjwQtgynk34Ej5jHimii35dqxBdsb00Cj3OqFxi";
    private static final String STRIPE_PUBLIC_KEY = "pk_test_51H2plbD8XywEs7fGe7oFOhljj83yyRzw8g67FhlIY2g2e3j9EvCRt5dQbqs0xpsdPygPBjMjJbXQnxsMtcOdV7sG00Cm2gGUKX";

    public ServiceFacture() {
        Stripe.apiKey = STRIPE_API_KEY;
    }

    @Override
    public void ajouter(Facture facture) throws SQLException {
        String req = "INSERT INTO invoices (reservation_id, amount, date, mode_de_paiement, condition_de_paiement) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(req)) {
            pst.setInt(1, facture.getReservationId());
            pst.setDouble(2, facture.getAmount());
            pst.setDate(3, Date.valueOf(facture.getDate()));
            pst.setString(4, facture.getModeDePaiement());
            pst.setString(5, facture.getConditionDePaiement());
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
        String req = "UPDATE invoices SET reservation_id = ?, amount = ?, date = ?, mode_de_paiement = ?, condition_de_paiement = ? WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(req)) {
            pst.setInt(1, facture.getReservationId());
            pst.setDouble(2, facture.getAmount());
            pst.setDate(3, Date.valueOf(facture.getDate()));
            pst.setString(4, facture.getModeDePaiement());
            pst.setString(5, facture.getConditionDePaiement());
            pst.setInt(6, facture.getId());
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
                        rs.getDate("date").toLocalDate(),
                        rs.getString("mode_de_paiement"),
                        rs.getString("condition_de_paiement")
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
                        rs.getDate("date").toLocalDate(),
                        rs.getString("mode_de_paiement"),
                        rs.getString("condition_de_paiement")
                );
                facturesList.add(facture);
            }
        }
        return facturesList;
    }

    public String createStripePaymentIntent(Facture facture) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (facture.getAmount() * 100))
                .setCurrency("eur")
                .setDescription("Paiement pour facture #" + facture.getReservationId())
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        return paymentIntent.getClientSecret();
    }

    public void processPayment(String paymentMethodId, Facture facture) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (facture.getAmount() * 100))
                .setCurrency("eur")
                .setPaymentMethod(paymentMethodId)
                .setConfirm(true)
                .setDescription("Paiement pour facture #" + facture.getReservationId())
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
    }

    public List<Integer> getReservationIds() throws SQLException {
        List<Integer> reservationIds = new ArrayList<>();
        String req = "SELECT reservation_id FROM invoices";
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                reservationIds.add(rs.getInt("reservation_id"));
            }
        }
        return reservationIds;
    }

}
