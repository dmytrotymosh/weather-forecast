package org.example;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String API_KEY = dotenv.get("API_KEY");
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new RuntimeException("API_KEY not set");
        }
        List<String> cities = List.of("Chisinau", "Madrid", "Kyiv", "Amsterdam");
        Gson gson = new Gson();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.weatherapi.com/v1/")
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//        WeatherApiService service = retrofit.create(WeatherApiService.class);
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
//            Call<Data> call = service.getData(city, API_KEY);
            try {
                HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
                if (response.statusCode() != 200) {
                    System.err.println("Error while receiving data for " + city + ", code: " + response.statusCode());
                    continue;
                }
                Data data = gson.fromJson(response.body(), Data.class);
//                Response<Data> response = call.execute();
//                if (!response.isSuccessful() || response.body() == null) {
//                    System.err.println("Error while receiving data for" + city + ", code: " + response.code() +
//                            ", message: " + response.message());
//                    continue;
//                }
//                Data data = response.body();
                if (data.forecast.forecastday == null || data.forecast.forecastday.isEmpty()) {
                    System.err.println("Data for " + city + " is empty");
                    continue;
                }
                ForecastDay forecast = data.forecast.forecastday.get(0);
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
//            } catch (IOException e) {
//                System.err.println(e.getMessage());
//            }
        }
    }
}

interface WeatherApiService {
    @GET("forecast.json?aqi=no&alerts=no")
    Call<Data> getData(@Query("q") String city, @Query("key") String apiKey);
}