package pl.sm_projekt_aplikacjatodo.model;

public class Weather {
    private int temperature;
    private int feelsLikeTemeperature;
    private double wind_speed;
    private int humidity;

    public Weather(int temperature, int feelsLikeTemeperature, double wind_speed, int humidity) {
        this.temperature = temperature;
        this.feelsLikeTemeperature = feelsLikeTemeperature;
        this.wind_speed = wind_speed;
        this.humidity = humidity;
    }

    public Weather() {

    }

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

    public double getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
