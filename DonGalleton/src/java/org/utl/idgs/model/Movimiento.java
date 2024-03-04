
package org.utl.idgs.model;

/**
 *
 * @author Alda
 */
public class Movimiento {
    private int idMovimiento;
    private String fechaMovimiento;
    private String tipoMovimiento;
    private float monto;
    private Venta venta;
    private MateriaPrima materiaPrima;

    public Movimiento(int idMovimiento, String fechaMovimiento, String tipoMovimiento, float monto, Venta venta, MateriaPrima materiaPrima) {
        this.idMovimiento = idMovimiento;
        this.fechaMovimiento = fechaMovimiento;
        this.tipoMovimiento = tipoMovimiento;
        this.monto = monto;
        this.venta = venta;
        this.materiaPrima = materiaPrima;
    }

    public Movimiento() {
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public String getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(String fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public MateriaPrima getMateriaPrima() {
        return materiaPrima;
    }

    public void setMateriaPrima(MateriaPrima materiaPrima) {
        this.materiaPrima = materiaPrima;
    }
}
