package Service;

import Entite.Facture;
import Utils.DataSource;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.FileOutputStream;
import java.io.IOException;
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

    // New method to generate a PDF of the Facture using Apache PDFBox
    public void generatePdf(Facture facture, String outputPath) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.newLineAtOffset(100, 750); // Position to start writing

        // Add content to the PDF
        contentStream.showText("Facture Détails");
        contentStream.newLine();
        contentStream.showText("ID Réservation: " + facture.getReservationId());
        contentStream.newLine();
        contentStream.showText("Montant: " + facture.getAmount());
        contentStream.newLine();
        contentStream.showText("Date: " + facture.getDate());

        contentStream.endText();
        contentStream.close();

        // Save the document
        document.save(outputPath);
        document.close();
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

    public List<Integer> getReservationIds() throws SQLException {
        List<Integer> reservationIds = new ArrayList<>();
        String req = "SELECT DISTINCT reservation_id FROM invoices";  // Modify query if needed
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                reservationIds.add(rs.getInt("reservation_id"));
            }
        }
        return reservationIds;
    }

}
