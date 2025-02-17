import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Resimler extends WebAppGui {

    public Resimler() {
    }

    public static ImageIcon resimYolla(String resourcePath) {            //resim yollamak için kullanılan metod
        try {
            BufferedImage resim = ImageIO.read(new File(resourcePath));        // Resim dosyasını belirtilen yoldan okuma


            return new ImageIcon(resim);
        } catch (IOException e) {
            e.printStackTrace();          // hatayı ekrana yazdır
        }
        System.out.println("Resim Dosyası Bulunamadı");
        return null;
    }
}
