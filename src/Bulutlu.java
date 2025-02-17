public class Bulutlu extends HavaDurumu {
    public Bulutlu() {
        super("Bulutlu");   //HavaDurumu değişkenine "Bulutlu" ifadesini yollar.
    }

    @Override
    public String uyariMesajiVer() {
        return "Hava bulutlu, hava durumu değişebilir.";
    }
}