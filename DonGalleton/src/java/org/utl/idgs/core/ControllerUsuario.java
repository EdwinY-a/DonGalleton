
package org.utl.idgs.core;

import java.sql.CallableStatement;
import org.utl.idgs.connection.ConexionMySQL;
import org.utl.idgs.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.utl.idgs.model.Movimiento;

/**
 *
 * @author Jorge
 */
public class ControllerUsuario {
    public Usuario login(String usuario, String contrasenia) throws SQLException{
        String query = "SELECT * FROM v_usuario vu WHERE vu.nombreUsuario = ? AND vu.contrasenia = ?";
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        Connection conn = connMySQL.open();
        
        PreparedStatement prepst = conn.prepareStatement(query);
        
        prepst.setString(1, usuario);
        prepst.setString(2, contrasenia);
        
        ResultSet rs = null;
        rs = prepst.executeQuery();
        
        Usuario u = null;
        
        if(rs.next())
            u = (fill(rs));
        
        rs.close();
        prepst.close();
        connMySQL.close();
        
        return u;
    }
    
    public int registrarUsuario(Usuario usuario) throws Exception {

        String sql = "{call insertarUsuario(?, ?, ?)}";

        int idUsuarioGenerado = -1;
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql);

        cstmt.setString(1, usuario.getNombreUsuario());
        cstmt.setString(2, usuario.getContrasenia());
        cstmt.setString(3, usuario.getRol());

        cstmt.registerOutParameter(4, Types.INTEGER);
        cstmt.executeUpdate();
        idUsuarioGenerado = cstmt.getInt(4);
        usuario.setIdUsuario(idUsuarioGenerado);
        

        cstmt.close();
        connMySQL.close();

        return idUsuarioGenerado;
    }
    public List<Usuario> getAll(String filtro) throws Exception {
          String sql = "SELECT * FROM v_usuario";

        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        ResultSet rs = pstmt.executeQuery();
        List<Usuario> usuario = new ArrayList<>();

        while (rs.next()) {
            usuario.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return usuario;
    }
    
    public Usuario getUsuario(int filtro) throws Exception {
        String sql = "SELECT * FROM v_usuario WHERE idUsuario ="+filtro+";" ;

        ConexionMySQL connMySQL = new ConexionMySQL(); 

        Connection conn = connMySQL.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        ResultSet rs = pstmt.executeQuery();
        Usuario usuarios = new Usuario();

        while (rs.next()) {
            usuarios = fill(rs);
        }
        rs.close();
        pstmt.close();
        connMySQL.close();
        return usuarios;
    }

    
    private Usuario fill(ResultSet rs) throws SQLException{
        Usuario u = new Usuario();
        
        u.setIdUsuario(rs.getInt("idUsuario"));
        u.setNombreUsuario(rs.getString("nombreUsuario"));
        u.setContrasenia(rs.getString("contrasenia"));
        u.setRol(rs.getString("rol"));
        u.setEstatus(rs.getInt("estatus"));
        
        return u;
    }
}
