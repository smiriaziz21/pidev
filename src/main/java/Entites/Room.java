package Entites;

public class Room {
    private int id;
    private int hotelId;
    private String roomNumber;
    private int capacity;

    public Room(int hotelId, String roomNumber, int capacity) {
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
    }

    public Room(int id, int hotelId, String roomNumber, int capacity) {
        this.id = id;
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", hotelId=" + hotelId +
                ", roomNumber='" + roomNumber + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
