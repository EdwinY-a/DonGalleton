
package org.utl.idgs.model;

/**
 *
 * @author Alda
 */
public class Producto {
    private int idProducto;
    private String nombreProducto;
    private double cantidadExistentes;
    private double precioVenta;
    private double precioProduccion;
    private String fotografia;
    private Medida medida;

    public Producto() {
    }

    public Producto(int idProducto, String nombreProducto, double cantidadExistentes, double precioVenta, double precioProduccion, String fotografia, Medida medida) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidadExistentes = cantidadExistentes;
        this.precioVenta = precioVenta;
        this.precioProduccion = precioProduccion;
        this.fotografia = fotografia;
        this.medida = medida;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public double getCantidadExistentes() {
        return cantidadExistentes;
    }

    public void setCantidadExistentes(double cantidadExistentes) {
        this.cantidadExistentes = cantidadExistentes;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public double getPrecioProduccion() {
        return precioProduccion;
    }

    public void setPrecioProduccion(double precioProduccion) {
        this.precioProduccion = precioProduccion;
    }

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    public Medida getMedida() {
        return medida;
    }

    public void setMedida(Medida medida) {
        this.medida = medida;
    }

    @Override
    public String toString() {
        return "Producto{" + "idProducto=" + idProducto + ", nombreProducto=" + nombreProducto + ", cantidadExistentes=" + cantidadExistentes + ", precioVenta=" + precioVenta + ", precioProduccion=" + precioProduccion + ", fotografia=" + fotografia + ", medida=" + medida + '}';
    }
}
