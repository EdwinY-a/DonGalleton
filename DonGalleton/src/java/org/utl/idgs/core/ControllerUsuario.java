
package org.utl.idgs.core;

import java.sql.CallableStatement;
import org.utl.idgs.connection.ConexionMySQL;
import org.utl.idgs.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

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
        u.setTelefono(rs.getString("telefono"));
        u.setLastToken(rs.getString("lastToken"));
        u.setDateLastToken(rs.getString("dateLastToken"));
        u.setEstatus(rs.getInt("estatus"));
        return u;
    }
    
    public void generarToken(int idUsuario, String token) throws SQLException{
        //Preparamos el procedure
        String sql="call generarToken(?, ?);";
        
        //Llamamos el objeto con el que vamos a conectar a base de datos
        ConexionMySQL connMySQL=new ConexionMySQL();
        
        //Abrimos conexion a base de datos
        Connection conn=connMySQL.open();
        
        //Llamamos al procedure que escribimos en la variable sql
        CallableStatement callsmnt=conn.prepareCall(sql);
        
        //En el primer ? del procedure le asignamos el id del usuario
        callsmnt.setInt(1, idUsuario);
        //En el segundo? del procedure le asignamos el token que se genero en el metodo de la clase del modelo
        callsmnt.setString(2, token);
        //Ejecutamos el procedure
        callsmnt.executeUpdate();
        
        //Cerramos el call
        callsmnt.close();
        //Cerramos conexion
        connMySQL.close();
    }
    
    /*
        Metodo para consultar si existe el token y es igual al que esta manipulando la app
    */
    public boolean validarToken(String t) throws Exception{
        //Declaramos una variable que nos regresa un falso
        boolean r=false;
        //Preparamos la consulta y la t es donde ira el parametro que recibe el metodo para asi quede completa la consulta
        String sql="SELECT * FROM v_usuario WHERE lastToken='"+t+"';";
        
        //Traemos el objeto con el que vamos a conectar a la base de datos
        ConexionMySQL conexionMySQL=new ConexionMySQL();
        
        //Abrimos conexion a la base de datos
        Connection connection=conexionMySQL.open();
        
        //Creamos el objeto que modela la sentencia
        Statement stmnt=connection.createStatement();
        //Preparamos la variable que va a recibir el reultado de la consulta y ejecutamos la consulta
        ResultSet rs=stmnt.executeQuery(sql);
        //Si hay resultados la variable nos la pone en verdadero
        if(rs.next())
            r=true;
        
        //Cerramos el statment
        stmnt.close();
        //Cerramos la conexion
        connection.close();
        //Cerramos el objeto
        conexionMySQL.close();
        
        //Nos regresa verdad si llego hasta aqui
        return r;
    }
    
    /*
        Metodo que elimina el token de la base de datos cada que se cierra la sesion
    */
   public boolean eliminarToken(Usuario u) throws Exception {
        boolean r = false;
        String query = "UPDATE usuario SET lastToken = '' WHERE idUsuario = ?";
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection connection = connMySQL.open();

        //CallableStatement cstmt = (CallableStatement) connection.prepareCall(query);
        PreparedStatement preparedStetement = connection.prepareCall(query);
        preparedStetement.setInt(1, u.getIdUsuario());
        preparedStetement.execute();
        r = true;
        preparedStetement.close();
        connection.close();
        connMySQL.close();
        return r;
    }  
}
