package Utils;

public class TestEmailSender {
    public static void main(String[] args) {
        String recipient = "azizfriscod@gmail.com";
        String subject = "Test Email with Attachment";
        String body = "Hello! This is a test email with PDF attachment.";
        String filePath = "C:\\Users\\azizf\\Desktop\\pidev\\src\\main\\resources\\azizpfe.pdf";

        EmailSender.sendEmail(recipient, subject, body, filePath);
    }
}