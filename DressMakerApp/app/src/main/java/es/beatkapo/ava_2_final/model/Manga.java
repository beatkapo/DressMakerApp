package es.beatkapo.ava_2_final.model;

public class Manga {
    public enum Tipo {LARGA, CORTA, SEMI}

    ;
    private Tipo tipo;
    private float anchoBrazo;
    private float anchoAnteBrazo;
    private float largoManga;

    public Manga(float anchoBrazo, float anchoAnteBrazo, float largoManga, Tipo tipo) {
        this.tipo = tipo;
        this.anchoBrazo = anchoBrazo;
        this.anchoAnteBrazo = anchoAnteBrazo;
        this.largoManga = largoManga;
    }

    public Manga() {
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
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
