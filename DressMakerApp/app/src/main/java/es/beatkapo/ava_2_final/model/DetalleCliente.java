package es.beatkapo.ava_2_final.model;

public class DetalleCliente {
    private String nombre;
    private String telefono;
    private float deuda;

    public DetalleCliente(String nombre, String telefono, float deuda) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.deuda = deuda;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public float getDeuda() {
        return deuda;
    }

    public void setDeuda(float deuda) {
        this.deuda = deuda;
    }
}
