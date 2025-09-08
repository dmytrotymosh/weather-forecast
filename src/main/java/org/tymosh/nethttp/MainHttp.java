package org.tymosh.nethttp;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import org.tymosh.model.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

public class MainHttp {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String API_KEY = dotenv.get("API_KEY");
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new RuntimeException("API_KEY not set");
        }
        List<String> cities = List.of("Chisinau", "Madrid", "Kyiv", "Amsterdam");
        Gson gson = new Gson();
        HttpClient client = HttpClient.newHttpClient();
        final int COLUMN_WIDTH = 20;
        System.out.printf("%" + COLUMN_WIDTH + "s%" + COLUMN_WIDTH + "s%" + COLUMN_WIDTH + "s%" +
                        COLUMN_WIDTH + "s%" + COLUMN_WIDTH + "s%" + COLUMN_WIDTH + "s\n",
                "Data Point", "MinTemp", "MaxTemp", "Humidity", "Wind Speed", "Wind Direction");
        for (String city : cities) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.weatherapi.com/v1/forecast.json?key=" +
                            API_KEY + "&q=" + city + "&days=1&aqi=no&alerts=no"))
                    .build();
            try {
                HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
                if (response.statusCode() != 200) {
                    System.err.println("Error while receiving data for " + city + ", code: " + response.statusCode());
                    continue;
                }
                Data data = gson.fromJson(response.body(), Data.class);
                if (data.forecast.forecastday == null || data.forecast.forecastday.isEmpty()) {
                    System.err.println("Data for " + city + " is empty");
                    continue;
                }
                ForecastDay forecast = data.forecast.forecastday.getFirst();
                double avgWindSpeed = forecast.getAvgWindSpeed();
                String windDir = forecast.getMostPopularWindDir();
                System.out.printf("%" + COLUMN_WIDTH + "s%" + COLUMN_WIDTH + ".2f%" + COLUMN_WIDTH + ".2f%" +
                                COLUMN_WIDTH + ".2f%" + COLUMN_WIDTH + ".2f%" + COLUMN_WIDTH + "s\n", city,
                        forecast.day.mintemp_c,
                        forecast.day.maxtemp_c,
                        forecast.day.avghumidity,
                        avgWindSpeed,
                        windDir);
            } catch (IOException | InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}