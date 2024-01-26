package pl.sm_projekt_aplikacjatodo.weatherApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("weather")
    Call<Weather> getWeatherData(@Query("city") String city);
}
