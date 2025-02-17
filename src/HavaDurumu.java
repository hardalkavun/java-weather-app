public abstract class HavaDurumu implements HavaDurumuMesaji {
    protected String havaDurumuAdi;

    public HavaDurumu(String havaDurumuAdi) {
        this.havaDurumuAdi = havaDurumuAdi;
    }

    public String getHavaDurumuAdi() {
        return havaDurumuAdi;
    }
}