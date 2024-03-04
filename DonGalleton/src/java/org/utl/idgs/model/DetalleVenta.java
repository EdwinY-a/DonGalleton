
package org.utl.idgs.model;

/**
 *
 * @author Alda
 */
public class DetalleVenta {
    private int idDetalleVenta;
    private double cantidad;
    private double subtotal;
    private Venta venta;
    private Producto producto;
    private Medida medida;

    public DetalleVenta(int idDetalleVenta, double cantidad, double subtotal, Venta venta, Producto producto, Medida medida) {
        this.idDetalleVenta = idDetalleVenta;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.venta = venta;
        this.producto = producto;
        this.medida = medida;
    }

    public DetalleVenta() {
    }

    public int getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(int idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Medida getMedida() {
        return medida;
    }

    public void setMedida(Medida medida) {
        this.medida = medida;
    }
}
