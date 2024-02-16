package es.beatkapo.ava_2_final.model;

import java.util.Date;
import java.util.List;

public class Trabajo {


    public enum Estado {PRUEBA, EN_PROCESO,TOMAR_MEDIDAS, TERMINADO}

    ;

    public enum Tipo {TRAJE, ARREGLO}

    ;

    private int id;
    private float precioTotal, pagado;
    private Estado estado;
    private Date fechaPedido, fechaFin ;
    private Cliente cliente;
    private Medida medida;
    private List<Prueba> pruebas;
    private Tipo tipo;


    public Trabajo() {
    }

    public Trabajo(float precioTotal, float pagado, Date fechaPedido, Date fechaFin, Cliente cliente, Estado estado, Tipo tipo, Medida medida, List<Prueba> pruebas) {
        this.tipo = tipo;
        this.precioTotal = precioTotal;
        this.pagado = pagado;
        this.fechaPedido = fechaPedido;
        this.fechaFin = fechaFin;
        this.cliente = cliente;
        this.estado = estado;
        this.medida = medida;
        this.pruebas = pruebas;
    }

    public List<Prueba> getPruebas() {
        return pruebas;
    }

    public void setPruebas(List<Prueba> pruebas) {
        this.pruebas = pruebas;
    }

    public Medida getMedida() {
        return medida;
    }

    public void setMedida(Medida medida) {
        this.medida = medida;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(float precioTotal) {
        this.precioTotal = precioTotal;
    }

    public float getPagado() {
        return pagado;
    }

    public void setPagado(float pagado) {
        this.pagado = pagado;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void pagar(float montoPago) {
        this.pagado += montoPago;
    }

    public void addPrueba(Prueba prueba) {
        pruebas.add(prueba);
    }

    public void removePrueba(Prueba prueba) {
        pruebas.remove(prueba);
    }
}
