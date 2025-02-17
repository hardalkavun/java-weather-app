import javax.swing.*;

public class HataYolla extends WebAppGui {
    public static void goster(String hataMesaji) {
        JOptionPane.showMessageDialog(null, hataMesaji, "Hata", JOptionPane.ERROR_MESSAGE); //null değerini döndürüp hata gözükmesini sağlar

    }
}
