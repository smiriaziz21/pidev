package Entite;

import java.util.List;

public class WeatherResponse {
    private List<Weather> weather;
    private Main main;
    private String name;

    // Getters et Setters
    public List<Weather> getWeather() { return weather; }
    public Main getMain() { return main; }
    public String getName() { return name; }
    public void setWeather(List<Weather> weather) { this.weather = weather; }
    public void setMain(Main main) { this.main = main; }
    public void setName(String name) { this.name = name; }

}
