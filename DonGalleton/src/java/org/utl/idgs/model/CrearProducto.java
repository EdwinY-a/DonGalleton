
package org.utl.idgs.model;

/**
 *
 * @author Alda
 */
public class CrearProducto {
    private int idCrearProducto;
    private float porcion;
    private Producto producto;
    private Medida medida;
    private MateriaPrima materiaPrima;

    public CrearProducto(int idCrearProducto, float porcion, Producto producto, Medida medida, MateriaPrima materiaPrima) {
        this.idCrearProducto = idCrearProducto;
        this.porcion = porcion;
        this.producto = producto;
        this.medida = medida;
        this.materiaPrima = materiaPrima;
    }

    public CrearProducto() {
    }

    public int getIdCrearProducto() {
        return idCrearProducto;
    }

    public void setIdCrearProducto(int idCrearProducto) {
        this.idCrearProducto = idCrearProducto;
    }

    public float getPorcion() {
        return porcion;
    }

    public void setPorcion(float porcion) {
        this.porcion = porcion;
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

    public MateriaPrima getMateriaPrima() {
        return materiaPrima;
    }

    public void setMateriaPrima(MateriaPrima materiaPrima) {
        this.materiaPrima = materiaPrima;
    }

    @Override
    public String toString() {
        return "CrearProducto{" + "idCrearProducto=" + idCrearProducto + ", porcion=" + porcion + ", producto=" + producto + ", medida=" + medida + ", materiaPrima=" + materiaPrima + '}';
    }
}
