package es.beatkapo.ava_2_final.model;

public class DetallePrueba {
    private String fecha;
    private String detalles;
    private int trabajoId;

    public DetallePrueba(String fecha, String detalles, int trabajoId) {
        this.fecha = fecha;
        this.detalles = detalles;
        this.trabajoId = trabajoId;
    }

    public DetallePrueba() {
    }

    public int getTrabajoId() {
        return trabajoId;
    }

    public void setTrabajoId(int trabajoId) {
        this.trabajoId = trabajoId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }


}
