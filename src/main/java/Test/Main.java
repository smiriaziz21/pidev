package Test;

import Controllers.AdminReservationsController;
import Controllers.ClientBookingController;
import Controllers.EventController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Utils.DataSource;
import java.io.IOException;
import java.sql.Connection;
/*
public class Main extends Application {

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        loadApplication();
    }

    private void loadApplication() {
        try {
            Connection conn = DataSource.getInstance().getCon();

            // Load Client Booking View
            FXMLLoader clientLoader = new FXMLLoader(getClass().getResource("/ClientBooking.fxml"));
            clientLoader.setController(new ClientBookingController(conn, 1));
            Parent clientRoot = clientLoader.load();

            // Load Admin Reservations View
            FXMLLoader adminLoader = new FXMLLoader(getClass().getResource("/AdminReservation.fxml"));
            adminLoader.setController(new AdminReservationsController(conn));
            Parent adminRoot = adminLoader.load();

            // Load Event Management View
            FXMLLoader eventLoader = new FXMLLoader(getClass().getResource("/EventManagement.fxml"));
            eventLoader.setController(new EventController(conn));
            Parent eventRoot = eventLoader.load();

            // Load Hotel Reservation View
            //FXMLLoader hotelLoader = new FXMLLoader(getClass().getResource("/HotelReservation.fxml"));
            //hotelLoader.setController(new HotelReservationController(conn));
            //Parent hotelRoot = hotelLoader.load();

            // Create a tabbed interface
            TabPane tabPane = new TabPane();
            Tab clientTab = new Tab("Client View", clientRoot);
            Tab adminTab = new Tab("Admin View", adminRoot);
            Tab eventTab = new Tab("Event Management", eventRoot);
            //Tab hotelTab = new Tab("Hotel Reservation", hotelRoot);
            tabPane.getTabs().addAll(clientTab, adminTab, eventTab);

            // Add Manual Reload Button
            Button reloadButton = new Button("Reload Application");
            reloadButton.setStyle("-fx-font-size: 14; -fx-padding: 5; -fx-background-color: #4CAF50; -fx-text-fill: white;");
            reloadButton.setOnAction(e -> {
                reloadApplication();
            });

            VBox rootLayout = new VBox(10, reloadButton, tabPane);

            // Set up and show the stage
            primaryStage.setScene(new Scene(rootLayout, 800, 600));
            primaryStage.setTitle("Event Management System");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading FXML: " + e.getMessage());
        }
    }

    private void reloadApplication() {
        try {
            System.out.println("Reloading application...");
            primaryStage.close(); // Close the current stage
            start(new Stage()); // Restart the application
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error reloading application: " + e.getMessage());
        }
    }
}*/
/*public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventManagement.fxml"));
        Parent root = loader.load();
        EventController controller = loader.getController(); // Ensure controller is set
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}*/
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignUp.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("page signup");
        stage.setScene(scene);
        stage.show();
    }
}


