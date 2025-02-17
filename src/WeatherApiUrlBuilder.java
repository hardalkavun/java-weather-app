public class WeatherApiUrlBuilder extends WebApp {

    private double latitude;
    private double longitude;
    private String timezone = "auto";  // Default timezone is auto
    private String hourly = "temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m";  // Default parameters

    // Getter ve setter enlem
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    // Getter ve setter boylam
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // Getter ve setter zamanAralığı
    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    // Getter ve setter saatlik değerler için
    public String getHourly() {
        return hourly;
    }

    public void setHourly(String hourly) {
        this.hourly = hourly;
    }

    // URL'yi tamamlamak için kullanılan metod
    public String buildUrl() {
        return "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude +
                "&longitude=" + longitude +
                "&hourly=" + hourly +
                "&timezone=" + timezone;
    }
}
