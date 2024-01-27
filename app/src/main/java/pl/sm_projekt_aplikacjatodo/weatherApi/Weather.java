package pl.sm_projekt_aplikacjatodo.weatherApi;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("temp")
    private int temperature;
    @SerializedName("feels_like")
    private int feelsLikeTemeperature;
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

    public int getFeelsLikeTemeperature() {
        return feelsLikeTemeperature;
    }

    public void setFeelsLikeTemeperature(int feelsLikeTemeperature) {
        this.feelsLikeTemeperature = feelsLikeTemeperature;
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
