import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WebApp {

    //girilen konum bilgisine göre konumun o anki hava durumunu verecek
    public static JSONObject havaDurumuVer(String konumAdi) {
        JSONArray konumBilgisi = getKonumBilgisi(konumAdi);


        JSONObject konum = (JSONObject) konumBilgisi.get(0);

        //girilen konum bilgisine göre değişkenlere eylem boylam atanır

        double enlem = (double) konum.get("latitude");
        double boylam = (double) konum.get("longitude");

        WeatherApiUrlBuilder urlBuilder = new WeatherApiUrlBuilder();
        urlBuilder.setLatitude(enlem);
        urlBuilder.setLongitude(boylam);

        //getter setter
        String urlString = urlBuilder.buildUrl();
        try {
            //API'Ye bağlan ve cevap ver
            HttpURLConnection connection = fetchApiResponse(urlString);

            //cevap kontrol edilir
            if (connection.getResponseCode() != 200) {        //HTTP response code 200 olduğu zaman düzgün şekilde bağlanıldığı anlamına gelir
                System.out.println("HATA: API'YE BAĞLANILAMADI");
                return null;
            }

            StringBuilder returnJson = new StringBuilder();
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                // read and store in the StringBuilder
                returnJson.append(scanner.nextLine());
            }
            scanner.close();
            connection.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject returnJsonObj = (JSONObject) parser.parse(String.valueOf(returnJson));

            //saatlik olarak dönüt almayı sağlar
            JSONObject hourly = (JSONObject) returnJsonObj.get("hourly"); //hourly anahtar kelime JSON objesini saatlik olarak döndürmeye yarayacak

            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);

            //sıcaklık kısmı
            JSONArray sicaklikBilgisi = (JSONArray) hourly.get("temperature_2m");
            double sicaklik = (double) sicaklikBilgisi.get(index);

            //hava durumu kısmı yağmurlu bulutlu
            JSONArray weathercode = (JSONArray) hourly.get("weather_code");
            //String havaDurumu=(String) havaDurumBilgisi.get(index);
            String havaDurumu = convertWeatherCode((long) weathercode.get(index));


            //nem alma kısmı
            JSONArray nemBilgisi = (JSONArray) hourly.get("relative_humidity_2m");
            long nemOrani = (long) nemBilgisi.get(index);

            //rüzgar hızını alma
            JSONArray ruzgarHizBilgisi = (JSONArray) hourly.get("wind_speed_10m");
            double ruzgarHizi = (double) ruzgarHizBilgisi.get(index);

            //build the weather json data object that we are going to access in our frontend
            JSONObject object = new JSONObject();
            object.put("temperature", sicaklik);
            object.put("weather_condition", havaDurumu);             //JSON nesnesi sayesinde API'den alınan bilgileri değişkenlere put fonksiyonu ile atama
            object.put("humidity", nemOrani);
            object.put("windspeed", ruzgarHizi);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //konum bilgisinin alınmasını sağlayacak
    public static JSONArray getKonumBilgisi(String konumAdi) {
        konumAdi = konumAdi.replaceAll(" ", "+"); //API bilgiyi alırken boşluk yerine + koyuyor hata çıkmaması için boşluk yerine + yazılmalı
        //getter setter yap
        String apiUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" + konumAdi + "&count=10&language=en&format=json";  // kullanacağımız API'nin adresinin gireceğimiz konuma göre ayarlanması
        try {
            // API bağlantısını al
            HttpURLConnection connection = fetchApiResponse(apiUrl);

            // Bağlantının başarılı olup olmadığını kontrol et (HTTP 200 başaro durumu)
            if (connection.getResponseCode() != 200) {
                // Bağlantı başarısızsa bir hata mesajı yazdır ve null döndür
                System.out.println("API'ye Bağlanılamadı");
                return null;
            } else {
                // API'den dönen veriyi bir StringBuilder'a kaydet
                StringBuilder result = new StringBuilder();
                Scanner scanner = new Scanner(connection.getInputStream()); // API yanıtını okuyacak bir tarayıcı oluştur

                // API'den gelen tüm satırları oku ve StringBuilder'a ekle
                while (scanner.hasNext()) {
                    result.append(scanner.nextLine());
                }
                scanner.close(); // Tarayıcıyı kapat
                connection.disconnect(); // Bağlantıyı kes

                // JSON yanıtını ayrıştır
                JSONParser parser = new JSONParser();
                JSONObject resultObj = (JSONObject) parser.parse(String.valueOf(result)); // JSON stringini nesneye dönüştür

                // JSON yanıtında "results" anahtarını al
                JSONArray konumBilgisi = (JSONArray) resultObj.get("results");
                // Not: "results" anahtarını Kullanmamız lazım Kullanmayınca Çalışmıyor !!!

                // Sonuç olarak konum bilgisi içeren JSONArray'i döndür
                return konumBilgisi;
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private static HttpURLConnection fetchApiResponse(String apiUrl) {
        try {
            // Verilen API URL'sini bir URL nesnesine çevir
            URL url = new URL(apiUrl);

            // URL üzerinden bir HTTP bağlantısı oluştur
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // HTTP isteği tipini "GET" olarak ayarla
            connection.setRequestMethod("GET");

            // Bağlantıyı başlat
            connection.connect();

            // Bağlantı nesnesini döndür
            return connection;

        } catch (IOException e) {
            // Hata oluşursa hata bilgilerini yazdır
            e.printStackTrace();
        }
        // Hata durumunda null döndür
        return null;
    };


    private static int findIndexOfCurrentTime(JSONArray timeList) {

        String currentTime = getCurrentTime();

        for (int i = 0; i < timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)) {

                return i;
            }
        }
        return 0;
    }

    public static String getCurrentTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // gün ay yıl zaman döngüsü API farklı şekilde okuduğu için düzeltme
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        String formattedTime = currentDateTime.format(formatter);
        return formattedTime;
    }

    // API'nin kendi sayısal kodlarına göre hava durum bilgisi var bunu okunabilir hale getirmemiz gerek
    public static String convertWeatherCode(long weathercode) {
        String havaDurumu = "";
        if (weathercode == 0L) {
            havaDurumu = "Güneşli";

        } else if (weathercode > 0L && weathercode <= 3L) {
            havaDurumu = "Bulutlu";
        } else if ((weathercode >= 51L && weathercode <= 67L) || (weathercode >= 80L && weathercode <= 99L)) {
            havaDurumu = "Yağmurlu";
        } else if (weathercode >= 71L && weathercode <= 77L) {
            havaDurumu = "Karlı";

        }
        return havaDurumu;
    }



















    /*
    public static JSONObject getWeather(String location) {
        JSONArray locationData = fetchLocationData(location);
        if (locationData == null || locationData.isEmpty()) {
            System.out.println("No location data found.");
            return null;
        }

        JSONObject firstLocation = (JSONObject) locationData.get(0);
        double latitude = (double) firstLocation.get("latitude");
        double longitude = (double) firstLocation.get("longitude");

        String apiUrl = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=Europe/London",
                latitude, longitude
        );

        try {
            HttpURLConnection connection = fetchApiResponse(apiUrl);
            if (connection.getResponseCode() != 200) {
                System.out.println("Failed to connect to weather API.");
                return null;
            }

            StringBuilder response = new StringBuilder();
            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
            }

            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(response.toString());

            JSONObject hourlyData = (JSONObject) jsonResponse.get("hourly");
            JSONArray timeArray = (JSONArray) hourlyData.get("time");
            int currentIndex = findCurrentTimeIndex(timeArray);

            double temperature = ((Number) ((JSONArray) hourlyData.get("temperature_2m")).get(currentIndex)).doubleValue();
            long humidity = ((Number) ((JSONArray) hourlyData.get("relative_humidity_2m")).get(currentIndex)).longValue();
            long weatherCode = ((Number) ((JSONArray) hourlyData.get("weather_code")).get(currentIndex)).longValue();
            double windSpeed = ((Number) ((JSONArray) hourlyData.get("wind_speed_10m")).get(currentIndex)).doubleValue();


            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", decodeWeatherCode(weatherCode));
            weatherData.put("humidity", humidity);
            weatherData.put("windspeed", windSpeed);

            return weatherData;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static JSONArray fetchLocationData(String location) {
        location = location.replace(" ", "+");
        String apiUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" + location + "&count=1&language=en&format=json";

        try {
            HttpURLConnection connection = fetchApiResponse(apiUrl);
            if (connection.getResponseCode() != 200) {
                System.out.println("Failed to connect to geocoding API.");
                return null;
            }

            StringBuilder response = new StringBuilder();
            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
            }

            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(response.toString());
            return (JSONArray) jsonResponse.get("results");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static HttpURLConnection fetchApiResponse(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            return connection;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int findCurrentTimeIndex(JSONArray timeArray) {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:00"));
        for (int i = 0; i < timeArray.size(); i++) {
            if (timeArray.get(i).equals(currentTime)) {
                return i;
            }
        }
        return 0;
    }

    private static String decodeWeatherCode(long code) {
        if (code == 0) return "Clear";
        if (code <= 3) return "Cloudy";
        if ((code >= 51 && code <= 67) || (code >= 80 && code <= 99)) return "Rainy";
        if (code >= 71 && code <= 77) return "Snowy";
        return "Unknown";
    }

     */
}





