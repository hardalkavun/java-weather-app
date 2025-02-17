import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SearchHistory {

    // Arama geçmişinin saklanacağı dosya yolu
    private static final String HISTORY_FILE = "src/search_history";

    // Arama geçmişini dosyaya kaydetme
    public static void addSearch(String cityName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE, true))) {
            // Yeni arama şehri dosyaya yazılır
            writer.write(cityName);
            writer.newLine(); // Yeni bir satır ekler
        } catch (IOException e) {
            e.printStackTrace(); // Hata olursa stack trace yazdırılır
        }
    }

    // Arama geçmişini dosyadan okuma
    public static List<String> getHistory() {
        List<String> history = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE))) {
            String line;
            // Dosyadaki her satırı okur ve listeye ekler
            while ((line = reader.readLine()) != null) {
                history.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Hata olursa stack trace yazdırılır
        }
        return history; // Okunan arama geçmişini döndürür
    }

    // Geçmişi gösteren bir pencere açma
    public static void showHistoryWindow() {
        List<String> history = getHistory(); // Arama geçmişini al

        // Eğer geçmiş boşsa kullanıcıya bilgi ver
        if (history.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Henüz bir arama yapılmamış.", "Arama Geçmişi", JOptionPane.INFORMATION_MESSAGE);
            return; // Eğer geçmiş boşsa fonksiyondan çık
        }

        // Arama geçmişini bir JTextArea içine ekle
        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false); // JTextArea düzenlenemez hale gelir
        for (String city : history) {
            historyArea.append(city + "\n"); // Her şehir adını yeni satırda ekle
        }

        // Geçmiş penceresini oluştur
        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setPreferredSize(new Dimension(300, 200)); // Pencere boyutunu ayarla

        // Arama geçmişini içeren pencereyi göster
        JOptionPane.showMessageDialog(null, scrollPane, "Arama Geçmişi", JOptionPane.INFORMATION_MESSAGE);
    }
}
