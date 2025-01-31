package Entites;

import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private int id;
    private String name;
    private String location;
    private int responsableHotelId;

    // Liste des chambres associées à cet hôtel
   //private List<Room> rooms;

    public Hotel(String name, String location, int responsableHotelId) {
        this.name = name;
        this.location = location;
        this.responsableHotelId = responsableHotelId;
      //  this.rooms = new ArrayList<>();
    }

    public Hotel(int id, String name, String location, int responsableHotelId) {
       this.id = id;
        this.name = name;
        this.location = location;
        this.responsableHotelId = responsableHotelId;
      //  this.rooms = new ArrayList<>();
    }



    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getResponsableHotelId() {
        return responsableHotelId;
    }

    public void setResponsableHotelId(int responsableHotelId) {
        this.responsableHotelId = responsableHotelId;
    }

   // public List<Room> getRooms() {
      //  return rooms;
   // }

   // public void setRooms(List<Room> rooms) {
     //   this.rooms = rooms;
   // }

   // public void addRoom(Room room) {
    //    this.rooms.add(room);
   // }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", responsableHotelId=" + responsableHotelId +
                '}';
    }
}
