import org.json.JSONObject;  // Assumes a JSON library such as org.json is included
import java.util.*;

// Weather Monitoring System
public class WeatherMonitoringSystem {
    private final String API_KEY = "your_api_key_here"; // Replace with your actual API key
    private final Map<String, List<Double>> cityTemperatures = new HashMap<>();
    private final Map<String, String> cityConditions = new HashMap<>();

    public void fetchWeatherData(List<String> cities) {
        for (String city : cities) {
            JSONObject weatherData = getWeatherDataFromAPI(city);
            if (weatherData != null) {
                double temp = weatherData.getJSONObject("main").getDouble("temp") - 273.15; // Convert Kelvin to Celsius
                cityTemperatures.putIfAbsent(city, new ArrayList<>());
                cityTemperatures.get(city).add(temp);
                cityConditions.put(city, weatherData.getJSONArray("weather").getJSONObject(0).getString("main"));
            }
        }
    }

    private JSONObject getWeatherDataFromAPI(String city) {
        // Placeholder for actual API call - returns dummy data for demonstration
        return new JSONObject("{\"main\": {\"temp\": 300}, \"weather\": [{\"main\": \"Clear\"}]}");
    }

    public void calculateDailySummary(String city) {
        List<Double> temps = cityTemperatures.getOrDefault(city, new ArrayList<>());
        double avgTemp = temps.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double maxTemp = temps.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        double minTemp = temps.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
        String dominantCondition = cityConditions.getOrDefault(city, "Clear");

        System.out.println("City: " + city);
        System.out.printf("Avg Temp: %.2f \u00B0C%n", avgTemp);
        System.out.printf("Max Temp: %.2f \u00B0C%n", maxTemp);
        System.out.printf("Min Temp: %.2f \u00B0C%n", minTemp);
        System.out.println("Dominant Condition: " + dominantCondition);
    }

    public void checkAndTriggerAlerts(String city, double threshold) {
        List<Double> temps = cityTemperatures.get(city);
        if (temps != null && temps.size() >= 2) {
            double lastTemp = temps.get(temps.size() - 1);
            double secondLastTemp = temps.get(temps.size() - 2);
            if (lastTemp > threshold && secondLastTemp > threshold) {
                System.out.println("Alert: Temperature exceeded threshold in " + city);
            }
        }
    }

    public static void main(String[] args) {
        WeatherMonitoringSystem weatherSystem = new WeatherMonitoringSystem();
        List<String> cities = Arrays.asList("Delhi", "Mumbai", "Chennai", "Bangalore", "Kolkata", "Hyderabad");
        weatherSystem.fetchWeatherData(cities);
        for (String city : cities) {
            weatherSystem.calculateDailySummary(city);
            weatherSystem.checkAndTriggerAlerts(city, 35.0); // Threshold example: 35\u00B0C
        }
    }
}
