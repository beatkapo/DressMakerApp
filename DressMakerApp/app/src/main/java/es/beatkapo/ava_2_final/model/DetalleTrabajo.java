package es.beatkapo.ava_2_final.model;

public class DetalleTrabajo {
    private String fechaEntrega;
    private float dineroPorPagar;
    private Trabajo.Estado estado;

    public DetalleTrabajo(String fechaEntrega, float dineroPorPagar, Trabajo.Estado estado) {
        this.fechaEntrega = fechaEntrega;
        this.dineroPorPagar = dineroPorPagar;
        this.estado = estado;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public float getDineroPorPagar() {
        return dineroPorPagar;
    }

    public void setDineroPorPagar(float dineroPorPagar) {
        this.dineroPorPagar = dineroPorPagar;
    }

    public Trabajo.Estado getEstado() {
        return estado;
    }

    public void setEstado(Trabajo.Estado estado) {
        this.estado = estado;
    }
}
