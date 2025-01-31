package Test;

import Entite.Activities;
import Service.ServiceActivities;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class Test {

    public static void main(String[] args) {

        ServiceActivities service = new ServiceActivities();


        Activities activity = new Activities(
                1,
                "Team Meeting",
                "Discuss project progress",
                LocalDateTime.of(2025, 1, 30, 10, 0),
                LocalDateTime.of(2025, 1, 30, 12, 0),
                "Meeting Room 1",
                2
        );

        // Add the activity to the database
        try {
            service.ajouter(activity);
            System.out.println("Activity added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding activity: " + e.getMessage());
        }

        // Retrieve and display all activities
        try {
            service.getAll().forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Error retrieving activities: " + e.getMessage());
        }
    }
}
