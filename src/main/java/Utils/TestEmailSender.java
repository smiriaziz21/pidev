package Utils;

public class TestEmailSender {
    public static void main(String[] args) {
        String recipient = "azizfriscod@gmail.com";
        String subject = "Test Email with Attachment";
        String body = "Hello! This is a test email with PDF attachment.";
        String filePath = "C:\\Users\\daliz\\Desktop\\new_pidev_proj\\tt\\src\\main\\resources\\AVAXIA-APPS - MidNight.pdf";

        EmailSender.sendEmail(recipient, subject, body, filePath);
    }
}