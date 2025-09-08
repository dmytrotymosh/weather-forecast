package org.tymosh.retrofit;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import org.tymosh.model.*;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;
import java.util.List;

public class MainRetrofit {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String API_KEY = dotenv.get("API_KEY");
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new RuntimeException("API_KEY not set");
        }
        List<String> cities = List.of("Chisinau", "Madrid", "Kyiv", "Amsterdam");
        Gson gson = new Gson();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        WeatherApiService service = retrofit.create(WeatherApiService.class);
        final int COLUMN_WIDTH = 20;
        System.out.printf("%" + COLUMN_WIDTH + "s%" + COLUMN_WIDTH + "s%" + COLUMN_WIDTH + "s%" +
                        COLUMN_WIDTH + "s%" + COLUMN_WIDTH + "s%" + COLUMN_WIDTH + "s\n",
                "Data Point", "MinTemp", "MaxTemp", "Humidity", "Wind Speed", "Wind Direction");
        for (String city : cities) {
            Call<Data> call = service.getData(city, API_KEY);
            try {
                Response<Data> response = call.execute();
                if (!response.isSuccessful() || response.body() == null) {
                    System.err.println("Error while receiving data for" + city + ", code: " + response.code() +
                            ", message: " + response.message());
                    continue;
                }
                Data data = response.body();
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
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
