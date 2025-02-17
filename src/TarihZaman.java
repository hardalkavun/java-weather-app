import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TarihZaman extends JPanel {

    private final JLabel zamanEtiketi;      // bu JLabel sayesin frame üstünde saat gözükecektir.

    public TarihZaman() {

        zamanEtiketi = new JLabel();
        zamanEtiketi.setFont(new Font("Dialog", Font.PLAIN, 18));
        zamanEtiketi.setForeground(Color.BLACK);
        add(zamanEtiketi);

        // Her saniye güncellenen bir zaman güncellemeyi başlatıyoruz
        Timer timer = new Timer(1000, e -> updateZaman());  // zaman güncellemesi
        timer.start();
    }

    private void updateZaman() {
        // Güncel tarihi al ve formatla     //dd/MM/yyyy---HH:mm
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm  dd/MM/yyyy");
        String formattedDate = now.format(formatter);

        // Etiketi güncelle
        zamanEtiketi.setText(formattedDate);
    }
}
