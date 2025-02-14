package Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WeatherController {

    @FXML
    private TextField locationField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label resultLabel;

    private static final String API_KEY = "f5a71f0400d0ac1782de71c6a05c766f";

    // ✅ Method to set location and date programmatically
    public void setLocationAndDate(String location, LocalDate date) {
        locationField.setText(location);
        datePicker.setValue(date);
    }

    @FXML
    public void fetchWeather() {
        String location = locationField.getText();
        LocalDate date = datePicker.getValue();

        if (location == null || location.isEmpty() || date == null) {
            resultLabel.setText("Please enter a valid location and date.");
            return;
        }

        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + API_KEY;

        new Thread(() -> {
            try {
                String weatherData = makeApiCall(url);
                updateUI(weatherData);
            } catch (IOException | InterruptedException e) {
                Platform.runLater(() -> resultLabel.setText("Error fetching weather data."));
                e.printStackTrace();
            }
        }).start();
    }

    private String makeApiCall(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private void updateUI(String weatherData) {
        Platform.runLater(() -> {
            try {
                JSONObject json = new JSONObject(weatherData);

                String city = json.getString("name");
                String weatherDescription = json.getJSONArray("weather").getJSONObject(0).getString("description");
                double tempKelvin = json.getJSONObject("main").getDouble("temp");
                double tempCelsius = tempKelvin - 273.15; // Conversion en °C

                String formattedText = String.format(
                        "🌍 ville: %s\n🌤 meteo: %s\n🌡 Temperature: %.2f°C",
                        city, weatherDescription, tempCelsius
                );

                resultLabel.setText(formattedText);
            } catch (Exception e) {
                resultLabel.setText("pas de donne meteo.");
                e.printStackTrace();
            }
        });
    }
}