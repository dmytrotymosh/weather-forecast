package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
    Forecast forecast;
}

class Forecast {
    List<ForecastDay> forecastday;
}

class ForecastDay {
    Day day;
    List<Hour> hour;
    public double getAvgWindSpeed() {
        if (hour.isEmpty()) return 0.0;
        double sum = 0;
        for (Hour h: hour) {
            sum += h.wind_kph;
        }
        return sum / hour.size();
    }
    public String getMostPopularWindDir() {
        Map<String, Integer> frequency = new HashMap<>();
        int max = 0;
        String required = "";
        for (Hour h: hour) {
            int newValue = frequency.getOrDefault(h.wind_dir, 0) + 1;
            frequency.put(h.wind_dir, newValue);
            if (newValue > max) {
                max = newValue;
                required = h.wind_dir;
            }
        }
        return required;
    }
}

class Day {
    double maxtemp_c;
    double mintemp_c;
    double avghumidity;
}

class Hour {
    double wind_kph;
    String wind_dir;
}