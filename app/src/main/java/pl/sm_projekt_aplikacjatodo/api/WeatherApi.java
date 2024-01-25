package pl.sm_projekt_aplikacjatodo.api;
import android.os.AsyncTask;
import android.util.Log;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApi extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... params) {
        try {
            URL url = new URL("https://api.api-ninjas.com/v1/weather?city=białystok");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("X-Api-Key", "gNPYXjsPS0huGuw4Otfdmg==slaTYz6WwtkmyGOA");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream responseStream = connection.getInputStream();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(responseStream);

                // Pobierz dane bezpośrednio z obiektu JSON, a nie z "fact"
                double temperature = root.path("temp").asDouble();
                return String.valueOf(temperature);
            } else {
                Log.e("WeatherApi", "HTTP error code: " + responseCode);
                return null;
            }
        } catch (IOException e) {
            Log.e("WeatherApi", "Error fetching weather data", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            // Tutaj możesz zrobić coś z otrzymanymi danymi, np. wyświetlić temperaturę
            Log.d("WeatherApi", "Temperature: " + result);
        } else {
            // Obsłuż błąd, jeśli nie uda się pobrać danych
            Log.e("WeatherApi", "Failed to fetch weather data");
        }
    }
}
