public class Gunesli extends HavaDurumu {
    public Gunesli() {
        super("Güneşli");  //HavaDurumu değişkenine "Güneşli" ifadesini yollar.
    }

    @Override
    public String uyariMesajiVer() {
        return "Hava güneşli, güneş gözlüğünüzü unutmayın!";
    }
}