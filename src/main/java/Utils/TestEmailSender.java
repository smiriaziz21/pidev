package Utils;

public class TestEmailSender {
    public static void main(String[] args) {
        // Replace with a valid recipient email
        String recipient = "azizfriscod@gmail.com";
        String subject = "Test Email";
        String body = "Hello! This is a test email from JavaFX.";

        // Call the sendEmail method
        EmailSender.sendEmail(recipient, subject, body);
    }
}
