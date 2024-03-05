
package org.utl.idgs.model;

import java.util.Date;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Alda
 */
public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private String contrasenia;
    private String rol;
    private int estatus;
    private String logsUser;
    private String lastToken;
    private String dateLastToken;


    public Usuario() {
    }

    public Usuario(int idUsuario, String nombreUsuario, String contrasenia, String rol, int estatus, String logsUser, String lastToken, String dateLastToken) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.rol = rol;
        this.estatus = estatus;
        this.logsUser = logsUser;
        this.lastToken = lastToken;
        this.dateLastToken = dateLastToken;
    }
    
    
    
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public String getLogsUser() {
        return logsUser;
    }

    public void setLogsUser(String logsUser) {
        this.logsUser = logsUser;
    }

    @Override
    public String toString() {
        return "Usuario{" + "idUsuario=" + idUsuario + ", nombreUsuario=" + nombreUsuario + ", contrasenia=" + contrasenia + ", rol=" + rol + ", estatus=" + estatus + ", logsUser=" + logsUser + '}';
    }

    public String getLastToken() {
        return lastToken;
    }

    public void setLastToken(String lastToken) {
        this.lastToken = lastToken;
    }

    public String getDateLastToken() {
        return dateLastToken;
    }

    public void setDateLastToken(String dateLastToken) {
        this.dateLastToken = dateLastToken;
    }
      public void setLastToken() {
    String u = this.getNombreUsuario();
    String p = this.getContrasenia();
    String k = new Date().toString();
    String x = (DigestUtils.sha256Hex(u + ";" + p + ";" + k));
    this.lastToken = x;
    this.dateLastToken = k; // Guardar la fecha de creación del token
}
}
