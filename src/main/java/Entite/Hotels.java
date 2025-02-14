package Entite;


public class Hotels {
    private int id;
    private String name;
    private String location;
    private int responsableHotelId;
    private int etoiles;
    // Liste des chambres associées à cet hôtel
    //private List<Room> rooms;

    public Hotels(String name, String location, int responsableHotelId, int etoiles) {
        this.name = name;
        this.location = location;
        this.responsableHotelId = responsableHotelId;
        this.etoiles = etoiles;
    }

    public Hotels(int id, String name, String location, int responsableHotelId, int etoiles) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.responsableHotelId = responsableHotelId;
        this.etoiles = etoiles;
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
    public int getEtoiles() {
        return etoiles;
    }

    public void setEtoiles(int etoiles) {
        this.etoiles = etoiles;
    }

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
