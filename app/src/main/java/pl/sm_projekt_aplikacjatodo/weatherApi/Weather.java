package pl.sm_projekt_aplikacjatodo.weatherApi;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("temp")
    private int temperature;
    @SerializedName("feels_like")
    private int feelsLikeTemperature;
    @SerializedName("wind_speed")
    private double windSpeed;
    @SerializedName("humidity")
    private int humidity;

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getFeelsLikeTemperature() {
        return feelsLikeTemperature;
    }

    public void setFeelsLikeTemperature(int feelsLikeTemperature) {
        this.feelsLikeTemperature = feelsLikeTemperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
