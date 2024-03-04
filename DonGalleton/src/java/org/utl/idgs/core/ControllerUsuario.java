
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

/**
 *
 * @author Alda
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
    
    public int insertarUsuario(Usuario usuario) throws Exception {

        String sql = "{call insertarUsuario(?, ?, ?,? ,?)}";

        int idUsuarioGenerado = -1;
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql);

        cstmt.setString(1, usuario.getNombreUsuario());
        cstmt.setString(2, usuario.getContrasenia());
        cstmt.setString(3, usuario.getRol());
        cstmt.setString(4, usuario.getLogsUser());
        System.out.println("LOGS " +usuario.getLogsUser());

        cstmt.registerOutParameter(5, Types.INTEGER);
        cstmt.executeUpdate();
        idUsuarioGenerado = cstmt.getInt(5);
        usuario.setIdUsuario(idUsuarioGenerado);
        

        cstmt.close();
        connMySQL.close();

        return idUsuarioGenerado;
    }
    
    public void desactivarUsuario(int idUsuario, String logs) throws Exception {
        String sql = "{CALL desActivarUsuario(?,?)};";
    
        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();
        
        
        CallableStatement cstmt = conn.prepareCall(sql);
 
        cstmt.setInt(1, idUsuario);
        cstmt.setString(2, logs);
        
        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }
    
    public void actualizarUsuario(Usuario usuario) throws Exception {

        String sql = "{call actualizarUsuario(?, ?, ?,?,?)}";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql); 
        
        cstmt.setInt(1, usuario.getIdUsuario());
        cstmt.setString(2, usuario.getNombreUsuario());
        cstmt.setString(3, usuario.getContrasenia());
        cstmt.setString(4, usuario.getRol());
        cstmt.setString(5, usuario.getLogsUser());
        

        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
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
        String sql = "SELECT * FROM usuario WHERE idUsuario ="+filtro+";" ;

        ConexionMySQL connMySQL = new ConexionMySQL(); 

        Connection conn = connMySQL.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        ResultSet rs = pstmt.executeQuery();
        Usuario user = new Usuario();

        while (rs.next()) {
            user = fill(rs);
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return user;
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
