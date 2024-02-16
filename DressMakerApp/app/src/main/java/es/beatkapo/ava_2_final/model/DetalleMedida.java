package es.beatkapo.ava_2_final.model;

public class DetalleMedida {
    private float espalda;
    private float cintura;
    private float pecho;
    private float falda;
    private String tipo;
    private float anchoBrazo;
    private float anchoAnteBrazo;
    private float largoManga;

    public DetalleMedida(float espalda, float cintura, float pecho, float falda, String tipo, float anchoBrazo, float anchoAnteBrazo, float largoManga) {
        this.espalda = espalda;
        this.cintura = cintura;
        this.pecho = pecho;
        this.falda = falda;
        this.tipo = tipo;
        this.anchoBrazo = anchoBrazo;
        this.anchoAnteBrazo = anchoAnteBrazo;
        this.largoManga = largoManga;
    }

    public DetalleMedida() {
    }

    public float getEspalda() {
        return espalda;
    }

    public void setEspalda(float espalda) {
        this.espalda = espalda;
    }

    public float getCintura() {
        return cintura;
    }

    public void setCintura(float cintura) {
        this.cintura = cintura;
    }

    public float getPecho() {
        return pecho;
    }

    public void setPecho(float pecho) {
        this.pecho = pecho;
    }

    public float getFalda() {
        return falda;
    }

    public void setFalda(float falda) {
        this.falda = falda;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public float getAnchoBrazo() {
        return anchoBrazo;
    }

    public void setAnchoBrazo(float anchoBrazo) {
        this.anchoBrazo = anchoBrazo;
    }

    public float getAnchoAnteBrazo() {
        return anchoAnteBrazo;
    }

    public void setAnchoAnteBrazo(float anchoAnteBrazo) {
        this.anchoAnteBrazo = anchoAnteBrazo;
    }

    public float getLargoManga() {
        return largoManga;
    }

    public void setLargoManga(float largoManga) {
        this.largoManga = largoManga;
    }
}
