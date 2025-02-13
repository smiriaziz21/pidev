package Utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import java.io.File;
import java.util.Properties;

public class EmailSender {

    public static void sendEmail(String recipient, String subject, String body, String filePath) {
        final String senderEmail = "azizfriscod@gmail.com";
        final String senderPassword = "dyrw haef gxed lsdo";


        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);


            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);


            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filePath);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(new File(filePath).getName());

            // Combine parts into multipart
            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);


            message.setContent(multipart);


            Transport.send(message);
            System.out.println("Email with attachment sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}