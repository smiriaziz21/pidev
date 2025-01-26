package Test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.imageio.IIOException;
import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/AjouterPersonne.fxml"));
       // FXMLLoader loader=new FXMLLoader(getClass().getResource("/AfficherPersonne.fxml"));

        Parent root=loader.load();
        Scene scene=new Scene(root);
        stage.setTitle("Ajouter");
        stage.setScene(scene);
        stage.show();
    }
}
