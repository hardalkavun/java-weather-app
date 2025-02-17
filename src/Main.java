import javax.swing.*;


public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {         //GUI'nin update almasını sağlar kullanıcı her yeni giriş yaptığında Frame yenilenir
            @Override
            public void run() {
                new WebAppGui().setVisible(true);         //nesneyi oluşturup ekrana frame gelmesini sağlar
            }

        });
    }
}
