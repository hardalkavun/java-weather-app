import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WebAppGui extends JFrame {
    private JLabel uyariMesaji;                             // Uyarı mesajını gösterecek olan kısım

    CustomException hataTest = new CustomException("Hata");           //Konsolda hatayı görebilmek için Custom Exception Sınıfından nesne oluşturma

    private JSONObject object;


    public WebAppGui() {        //constructor

        super("Hava Durumu");
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Uygulamayı kapatınca programı sonlandırır.
        setSize(450, 650);        //frame boyutunu ayarlamak için kullan (duruma göre değiştir)
        setLocationRelativeTo(null);           //Frame ekranın ortasında açılır.
        setLayout(null);
        setResizable(false);   //boyut değiştirmeyi engelledik kullanıcı boyutu değiştiremiyecek

        GuiStyle();              // add GuiStyle(); !!!
    }

    private void GuiStyle() {
        JTextField aramaYap = new JTextField();

        aramaYap.setBounds(50, 40, 300, 40);   //arama yapılan yerin boyutları ve konumu
        aramaYap.setFont(new Font("MV Boli", Font.PLAIN, 20));  //arama yapılan yerin görünümü
        add(aramaYap);                                          //aramaYap isimli text girilen kısmı GUI'ye ekledik

        ImageIcon ikonResim = new ImageIcon("src/assets/weather.png");
        setIconImage(ikonResim.getImage());


        TarihZaman tarihZamanPaneli = new TarihZaman();
        tarihZamanPaneli.setBounds(60, 5, 300, 40); // Konumu ve boyutu ayarlıyoruz
        add(tarihZamanPaneli);

        uyariMesaji = new JLabel("Uyarı mesajı burada görünecek.");
        uyariMesaji.setBounds(50, 580, 350, 30);
        uyariMesaji.setFont(new Font("Dialog", Font.ITALIC, 14));
        uyariMesaji.setHorizontalAlignment(SwingConstants.CENTER);
        add(uyariMesaji);


        JButton historyButton = new JButton("Arama Geçmişi");           //arama geçmişi butonunu ekleme
        historyButton.setBounds(50, 90, 140, 40);   // Adjust the position and size
        historyButton.setFont(new Font("Dialog", Font.BOLD, 13));

        // arama geçmişi
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // arama geçmişi butonuna tıklandığı zaman yeni panel açılır
                SearchHistory.showHistoryWindow();
            }
        });

        add(historyButton);  //arama geçmişi butonu eklendi


        //hava durumunu belirten resmin olduğu kısım
        JLabel havaResim = new JLabel(Resimler.resimYolla("src/assets/cloudy.png"));
        havaResim.setBounds(0, 125, 450, 217);
        add(havaResim);

        //sıcaklık göstergesi
        JLabel sicaklik = new JLabel("--℃");
        sicaklik.setBounds(0, 350, 450, 54);         //sınır belirleme      (büyütülebilir!!)
        sicaklik.setFont(new Font("Dialog", Font.BOLD, 30));  //sıcaklık yazısının fontu
        sicaklik.setHorizontalAlignment(SwingConstants.CENTER);      //sıcaklık yazısı bunu yazmamız sayesinde ekranın ortasına geldi
        add(sicaklik);

        //hava durumunun nasıl olduğunu gösteren yazı
        JLabel havaDurumu = new JLabel("Bulutlu");
        havaDurumu.setBounds(0, 400, 450, 54);
        havaDurumu.setFont(new Font("MV Boli", Font.ITALIC, 30));
        havaDurumu.setHorizontalAlignment(SwingConstants.CENTER);
        add(havaDurumu);

        // (resim) havadaki nem oranı
        JLabel nemResmi = new JLabel(Resimler.resimYolla("src/assets/humidity.png"));
        nemResmi.setBounds(15, 500, 74, 66);
        add(nemResmi);

        // (text) havadaki nem oranı
        JLabel nemOran = new JLabel("<html><b>Nem Oranı</b> --</html>");
        nemOran.setBounds(90, 500, 86, 55);
        nemOran.setFont(new Font("Dialog", Font.PLAIN, 15));
        add(nemOran);

        //rüzgar resmi
        JLabel ruzgarResim = new JLabel(Resimler.resimYolla("src/assets/windspeed.png"));
        ruzgarResim.setBounds(240, 500, 74, 66);
        add(ruzgarResim);

        //rüzgar yazısı
        JLabel ruzgarYazi = new JLabel("<html><b>Rüzgar Hızı</b> --</html>");
        ruzgarYazi.setBounds(330, 500, 85, 55);
        ruzgarYazi.setFont(new Font("Dialog", Font.PLAIN, 15));
        add(ruzgarYazi);

        //arama buttonu
        JButton aramaButton = new JButton(Resimler.resimYolla("src/assets/search.png"));
        aramaButton.setBounds(360, 40, 50, 40); // arama butonunu ekranın sağ tarafına taşıdım başta sol taraftaydı
        aramaButton.setFocusable(false);
        aramaButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                //Kullanıcıdan konum bilgisi al
                String userInput = aramaYap.getText();

                //kullanıcı gereksiz yere boşluk koyarsa o kısımları silmek için
                if (userInput.replaceAll("\\s", "").length() <= 0) {

                    hataTest.printStackTrace(); //hatayı konsolda görebilmek için Custom Exception Kullanımı
                    HataYolla.goster("Lütfen geçerli bir şehir adı girin.");
                    return;
                }

                // Giriş doğrulama: boşsa veya sayısal değerler/özel karakterler içeriyorsa
                if (userInput.isEmpty()) {

                    hataTest.printStackTrace();//hatayı konsolda görebilmek için Custom Exception Kullanımı
                    HataYolla.goster("Lütfen geçerli bir şehir adı girin.");
                    return;
                }
                // a-z ve A-Z arasındaki sadece alfabede olan değerlerin yazıldığını kontrol eder
                if (!userInput.matches("^[a-zA-Z\\s]+$")) {

                    hataTest.printStackTrace(); //hatayı konsolda görebilmek için Custom Exception Kullanımı
                    HataYolla.goster("Şehir adı yalnızca harf ve boşluk içermelidir.");
                    return;
                }
                // Save to search history
                SearchHistory.addSearch(userInput);

                // Hava durumu bilgisini al
                object = WebApp.havaDurumuVer(userInput);

                // Hata durumunda kullanıcıya bilgi ver
                if (object == null) {

                    hataTest.printStackTrace(); //hatayı konsolda görebilmek için Custom Exception Kullanımı

                    HataYolla.goster("Hava durumu bilgisi alınamadı. Lütfen şehir adını kontrol edin.");
                    return;
                }

                //alınan hava durumuna göre görselin değişmesi

                String havaDurumuStr = (String) object.get("weather_condition");
                HavaDurumu havaDurumu1;

                //switch case yapısı ile birlikte havanın durumuna göre resim ve yazı yollanır
                switch (havaDurumuStr) {
                    case "Güneşli":
                        havaResim.setIcon(Resimler.resimYolla("src/assets/clear.png"));
                        havaDurumu1 = new Gunesli();
                        break;
                    case "Bulutlu":
                        havaResim.setIcon(Resimler.resimYolla("src/assets/cloudy.png"));
                        havaDurumu1 = new Bulutlu();
                        break;
                    case "Yağmurlu":
                        havaResim.setIcon(Resimler.resimYolla("src/assets/rain.png"));
                        havaDurumu1 = new Yagmurlu();
                        break;
                    case "Karlı":
                        havaResim.setIcon(Resimler.resimYolla("src/assets/snow.png"));
                        havaDurumu1 = new Karli();
                        break;
                    default:
                        havaDurumu1 = null;
                        break;
                }
                //hava durumu bilgisi boş olarak döndürülürse ekrana hata mesajını yazdır
                if (havaDurumu1 != null) {
                    uyariMesaji.setText(havaDurumu1.uyariMesajiVer());
                } else {
                    uyariMesaji.setText("Hava durumu bilgisi alınamadı");
                }


                //sıcaklık yazısını güncellemek gerekli
                double temp = (double) object.get("temperature");
                sicaklik.setText(temp + " ℃");

                //hava durumu text'ini güncelle
                havaDurumu.setText(havaDurumuStr);


                //nem oranı güncelle
                long humidity = (long) object.get("humidity");
                nemOran.setText("<html><b>Nem Oranı</b>" + humidity + "%</html>");
                nemOran.setFont(new Font("Dialog", Font.PLAIN, 15));


                //rüzgarhızı güncelle
                double windspeed = (double) object.get("windspeed");
                ruzgarYazi.setText("<html><b>Rüzgar Hızı</b>" + windspeed + "km/h</html>");
                ruzgarYazi.setFont(new Font("Dialog", Font.PLAIN, 15));


            }
        });

        aramaButton.setBackground(Color.GRAY);
        add(aramaButton);  //arama butonunu Frame'e ekleme

    }
}