public class Yagmurlu extends HavaDurumu {
    public Yagmurlu() {
        super("Yağmurlu");  //HavaDurumu değişkenine "Yağmurlu" ifadesini yollar.
    }

    @Override
    public String uyariMesajiVer() {
        return "Bugün yağmurlu şemsiyenizi unutmayın";
    }
}