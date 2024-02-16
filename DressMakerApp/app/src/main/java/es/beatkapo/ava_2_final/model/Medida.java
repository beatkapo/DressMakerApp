package es.beatkapo.ava_2_final.model;

public class Medida {
    private int id, trabajoId;
    private float espalda, pecho, cintura, falda;
    private Manga manga;

    public Medida(float espalda, float pecho, float cintura, float falda, Manga manga, int trabajoId) {
        this.espalda = espalda;
        this.pecho = pecho;
        this.cintura = cintura;
        this.falda = falda;
        this.manga = manga;
        this.trabajoId = trabajoId;
    }

    public Medida() {
    }

    public int getTrabajoId() {
        return trabajoId;
    }

    public void setTrabajoId(int trabajoId) {
        this.trabajoId = trabajoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getEspalda() {
        return espalda;
    }

    public void setEspalda(float espalda) {
        this.espalda = espalda;
    }

    public float getPecho() {
        return pecho;
    }

    public void setPecho(float pecho) {
        this.pecho = pecho;
    }

    public float getCintura() {
        return cintura;
    }

    public void setCintura(float cintura) {
        this.cintura = cintura;
    }

    public float getFalda() {
        return falda;
    }

    public void setFalda(float falda) {
        this.falda = falda;
    }

    public Manga getManga() {
        return manga;
    }

    public void setManga(Manga manga) {
        this.manga = manga;
    }

    @Override
    public String toString() {
        return "Medida{" +
                "id=" + id +
                ", trabajoId=" + trabajoId +
                ", espalda=" + espalda +
                ", pecho=" + pecho +
                ", cintura=" + cintura +
                ", falda=" + falda +
                ", manga=" + manga +
                '}';
    }
}
