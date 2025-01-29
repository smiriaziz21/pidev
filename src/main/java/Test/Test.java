package Test;

import Entite.Activities;
import Service.ServiceActivities;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class Test {

    public static void main(String[] args) {

        ServiceActivities service = new ServiceActivities();

        // Create a new activity with all necessary fields
        Activities activity = new Activities(
                1, // id_event
                "Team Meeting", // name
                "Discuss project progress", // description
                LocalDateTime.of(2025, 1, 30, 10, 0), // start_date
                LocalDateTime.of(2025, 1, 30, 12, 0), // end_date
                "Meeting Room 1", // location
                2 // responsible_id (nullable: pass `null` if not applicable)
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
