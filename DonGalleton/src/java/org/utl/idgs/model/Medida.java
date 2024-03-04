
package org.utl.idgs.model;

/**
 *
 * @author Alda
 */
public class Medida {
    private int idMedida;
    private String tipoMedida;

    public Medida(int idMedida, String tipoMedida) {
        this.idMedida = idMedida;
        this.tipoMedida = tipoMedida;
    }

    public Medida() {
    }

    public int getIdMedida() {
        return idMedida;
    }

    public void setIdMedida(int idMedida) {
        this.idMedida = idMedida;
    }

    public String getTipoMedida() {
        return tipoMedida;
    }

    public void setTipoMedida(String tipoMedida) {
        this.tipoMedida = tipoMedida;
    }

    @Override
    public String toString() {
        return "Medida{" + "idMedida=" + idMedida + ", tipoMedida=" + tipoMedida + '}';
    }
}
