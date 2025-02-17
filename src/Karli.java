public class Karli extends HavaDurumu {
    public Karli() {
        super("Karli");  //HavaDurumu değişkenine "Karli" ifadesini yollar.
    }

    @Override
    public String uyariMesajiVer() {    //kullanıcıya uyarı mesajını gösteren metot.
        return "Hava karlı dikkatli olun";
    }
}