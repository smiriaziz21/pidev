package Service;

import Entite.WeatherResponse;
import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherService {
    private static final String API_KEY = "b1b15e88fa797225412429c1c50c122a1";
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s,%s&appid=%s&units=metric&lang=fr";


    public String getWeatherData(String city, String countryCode) throws Exception {

        String formattedUrl = String.format(API_URL, city, countryCode, API_KEY);
        System.out.println("[DEBUG] URL: " + formattedUrl);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(formattedUrl))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("[DEBUG] Response Code: " + response.statusCode());
        System.out.println("[DEBUG] Full Response: " + response.body());

        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            WeatherResponse weatherResponse = gson.fromJson(response.body(), WeatherResponse.class);

            // Extraction des données
            String cityName = weatherResponse.getName();
            double temperature = weatherResponse.getMain().getTemp();
            String description = weatherResponse.getWeather().get(0).getDescription();

            return String.format("%s - %.1f°C, %s", cityName, temperature, description);
        }
        return "Météo non disponible";
    }
}
