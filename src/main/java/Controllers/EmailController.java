package Controllers;

import Utils.EmailSender;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EmailController {

    @FXML
    private TextField recipientField;

    @FXML
    private TextField subjectField;

    @FXML
    private TextField bodyField;

    @FXML
    private void sendEmail() {
        String recipient = recipientField.getText();
        String subject = subjectField.getText();
        String body = bodyField.getText();
        String filePath = "C:\\Users\\daliz\\Desktop\\new_pidev_proj\\tt\\src\\main\\resources\\AVAXIA-APPS - MidNight.pdf";

        EmailSender.sendEmail(recipient, subject, body, filePath);
    }
}