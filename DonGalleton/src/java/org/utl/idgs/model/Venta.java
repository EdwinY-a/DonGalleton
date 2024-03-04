
package org.utl.idgs.model;

/**
 *
 * @author Alda
 */
public class Venta {
    private int idVenta;
    private String fechaVenta;
    private float total;

    public Venta(int idVenta, String fechaVenta, float total) {
        this.idVenta = idVenta;
        this.fechaVenta = fechaVenta;
        this.total = total;
    }

    public Venta() {
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
