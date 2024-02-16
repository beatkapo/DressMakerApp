package es.beatkapo.ava_2_final.model;

import java.util.Date;
import java.util.List;

public class Prueba {
    private int id;
    private Date fecha;
    private String detalles;
    private int trabajoId;
    private List<String> imagenes;

    public Prueba(Date fecha, String detalles, int trabajoId) {
        this.fecha = fecha;
        this.detalles = detalles;
        this.trabajoId = trabajoId;
    }

    public Prueba() {
    }

    public List<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public int getTrabajoId() {
        return trabajoId;
    }

    public void setTrabajoId(int trabajoId) {
        this.trabajoId = trabajoId;
    }
}
