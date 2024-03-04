/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.idgs.model;

/**
 *
 * @author Usuario
 */
public class Logs {
    private int id;
    private String fecha;
    private String usuario;
    private String procedimiento;

    public Logs() {
        
    }

    public Logs(int id, String fecha, String usuario, String procedimiento) {
        this.id = id;
        this.fecha = fecha;
        this.usuario = usuario;
        this.procedimiento = procedimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    @Override
    public String toString() {
        return "Logs{" + "id=" + id + ", fecha=" + fecha + ", usuario=" + usuario + ", procedimiento=" + procedimiento + '}';
    }

    
    
}
