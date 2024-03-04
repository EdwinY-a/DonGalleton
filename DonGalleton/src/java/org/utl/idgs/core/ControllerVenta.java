
package org.utl.idgs.core;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.utl.idgs.connection.ConexionMySQL;
import org.utl.idgs.model.Medida;
import org.utl.idgs.model.Movimiento;
import org.utl.idgs.model.Producto;
import org.utl.idgs.model.Venta;

/**
 *
 * @author Alda
 */
public class ControllerVenta {
    public int insertarVenta(Venta v) throws Exception {
        String sql = "{call insertarVenta(?, ?, ?)}";
       
        int idVentaGenerado = -1;
        
        Movimiento m = new Movimiento();
        ControllerMovimiento cm = new ControllerMovimiento();
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql);
        
        cstmt.setString(1, v.getFechaVenta());
        cstmt.setDouble(2, v.getTotal());
        cstmt.registerOutParameter(3, Types.INTEGER);
        cstmt.executeUpdate();
        idVentaGenerado = cstmt.getInt(3);
        v.setIdVenta(idVentaGenerado);
        
        m.setVenta(v);
        cm.insertarMovimientoVenta(m);

        cstmt.close();
        connMySQL.close();

        return idVentaGenerado;
    }
    
     public void actualizarVenta(Venta v) throws Exception {
        String sql = "{call actualizarVenta(?, ?,?)}"; 

        Movimiento m = new Movimiento();
        ControllerMovimiento cm = new ControllerMovimiento();
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql); 
        
        cstmt.setString(2, v.getFechaVenta());
        cstmt.setDouble(3, v.getTotal());
        
        
        cstmt.setInt(1, v.getIdVenta());

        cstmt.executeUpdate();
        
        m.setVenta(v);
        cm.actualizarMovimientoIngreso(m);

        cstmt.close();
        connMySQL.close();
    }
     
      public List<Venta> getAll(String filtro) throws Exception {
        String sql = "SELECT * FROM v_venta;" ;

        ConexionMySQL connMySQL = new ConexionMySQL(); 

        Connection conn = connMySQL.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        ResultSet rs = pstmt.executeQuery();
        List<Venta> ventas = new ArrayList<>();

        while (rs.next()) {
            ventas.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return ventas;
    }
    
   
    private Venta fill(ResultSet rs) throws Exception {
        Venta v = new Venta();
        
        v.setIdVenta(rs.getInt("idVenta"));
        v.setFechaVenta(rs.getString("fechaVenta"));
        v.setTotal((float) rs.getDouble("total"));  
       
        return v;
    }   
}
