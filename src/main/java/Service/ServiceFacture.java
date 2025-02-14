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
        String req = "SELECT id FROM reservations WHERE status = 'approved'"; // Ensure 'approved' is correctly formatted

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                reservationIds.add(rs.getInt("id")); // ✅ Corrected column name
            }
        }
        return reservationIds;
    }

    public void generatePdf(Facture facture, String outputPath) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4); // Standard A4 format
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        float marginLeft = 30;
        float startY = 750; // Starting position for the text
        // FACTURE#ID (Centré et Agrandi)
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20); // Taille augmentée
        float textWidth = PDType1Font.HELVETICA_BOLD.getStringWidth("FACTURE#" + facture.getReservationId()) / 1000 * 20;
        float centerX = (PDRectangle.A4.getWidth() - textWidth) / 2; // Centrage horizontal
        contentStream.newLineAtOffset(centerX, startY);
        contentStream.showText("FACTURE#" + facture.getReservationId());
        contentStream.endText();
        startY -= 30; // Plus d'espace après le titre
        // Invoice Number, Date, and Client Info
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        drawText(contentStream, marginLeft, startY, "Numéro de Facture : " + facture.getReservationId());
        startY -= 20;
        drawText(contentStream, marginLeft, startY, "Date : " + facture.getDate());
        startY -= 20;
        drawText(contentStream, marginLeft, startY, "Client : Mohamed Iheb Ounalli");
        startY -= 20;
        drawText(contentStream, marginLeft, startY, "Adresse : Djerba, Tunisia");
        startY -= 40;
        // Invoice Details (Amount, Reservation ID, Payment Mode)
        drawText(contentStream, marginLeft, startY, "Montant : " + facture.getAmount() + " €");
        startY -= 20;
        drawText(contentStream, marginLeft, startY, "Réservation : " + facture.getReservationId());
        startY -= 20;
        drawText(contentStream, marginLeft, startY, "Mode de Paiement : Carte bancaire");
        startY -= 40;
        // Description
        drawText(contentStream, marginLeft, startY, "Description");
        startY -= 20;
        drawText(contentStream, marginLeft, startY, "Paiement pour l'événement de gestion de factures,");
        startY -= 15;
        drawText(contentStream, marginLeft, startY, "effectué le 07/02/2025.");
        startY -= 30;
        // Conditions de paiement
        drawText(contentStream, marginLeft, startY, "Conditions de Paiement :");
        startY -= 20;
        drawText(contentStream, marginLeft, startY, "- Le paiement doit être effectué avant le 31/02/2025.");
        startY -= 15;
        drawText(contentStream, marginLeft, startY, "- En cas de retard, la réservation doit être annulée.");
        startY -= 30;
// Footer (Signature à droite)
        String signatureText = "Signature";
        float signatureWidth = PDType1Font.HELVETICA.getStringWidth(signatureText) / 1000 * 12;
        float rightX = PDRectangle.A4.getWidth() - signatureWidth - 50; // Position à droite avec une marge
        drawText(contentStream, rightX, startY, signatureText);
        // Close stream and save the document
        contentStream.close();
        document.save(outputPath);
        document.close();
    }
    // Utility function to draw text on the page
    private void drawText(PDPageContentStream contentStream, float x, float y, String text) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }
    // Utility function to wrap text to a specified length
    private List<String> wrapText(String text, int maxLength) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        for (String word : words) {
            if (currentLine.length() + word.length() + 1 > maxLength) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder();
            }
            currentLine.append(word).append(" ");
        }
        lines.add(currentLine.toString().trim());
        return lines;
    }
    // Helper method to draw a rectangle
    private void drawRectangle(PDPageContentStream contentStream, float x, float y, float width, float height) throws IOException {
        contentStream.moveTo(x, y);
        contentStream.lineTo(x + width, y);
        contentStream.lineTo(x + width, y - height);
        contentStream.lineTo(x, y - height);
        contentStream.closePath();
        contentStream.stroke();
    }
    // Method to save the PDF
    public void savePdf(Facture facture) {
        try {
            String outputPath = "Facture_" + facture.getId() + ".pdf";
            generatePdf(facture, outputPath);
            System.out.println("PDF generated at: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to save the PDF


}
