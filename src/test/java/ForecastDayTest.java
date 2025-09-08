import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tymosh.model.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ForecastDayTest {
    ForecastDay forecastDay = new ForecastDay();
    @BeforeEach
    void setUp() {
        forecastDay.hour = List.of(
                new Hour(2.4, "SE"),
                new Hour(3.6, "NE"),
                new Hour(4.8, "SE"),
                new Hour(5.0, "SE"),
                new Hour(6.2, "S"),
                new Hour(7.4, "W")
        );
    }
    @Test
    void testAvgWindSpeed() {
        double avgWindSpeed = forecastDay.getAvgWindSpeed();
        assertEquals(4.9, avgWindSpeed, 0.001);
    }
    @Test
    void testAvgWindDirection() {
        String windDir = forecastDay.getMostPopularWindDir();
        assertEquals("SE", windDir);
    }
}