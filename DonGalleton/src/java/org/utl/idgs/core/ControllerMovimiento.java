
package org.utl.idgs.core;

import org.utl.idgs.connection.ConexionMySQL;
import org.utl.idgs.model.Movimiento;
import org.utl.idgs.model.Venta;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.utl.idgs.model.MateriaPrima;
import org.utl.idgs.model.Medida;
import org.utl.idgs.model.Resultado;

/**
 *
 * @author Alda
 */
public class ControllerMovimiento {
    public int insertarMovimientoVenta(Movimiento m) throws SQLException{
        String query = "{CALL insertarMovimientoIngreso(?, ?, ?, ?, ?)}";
        
        int idMovimientoGenerado = -1;
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        Connection conn = connMySQL.open();
        
        CallableStatement clblsmt = conn.prepareCall(query);
        
        clblsmt.setString(1, m.getVenta().getFechaVenta());
        clblsmt.setString(2, "Ingreso");
        clblsmt.setDouble(3, m.getVenta().getTotal());
        clblsmt.setInt(4, m.getVenta().getIdVenta());
        
        clblsmt.registerOutParameter(5, Types.INTEGER);
        
        clblsmt.executeUpdate();
        
        idMovimientoGenerado = clblsmt.getInt(5);
        m.setIdMovimiento(idMovimientoGenerado);
        
        clblsmt.close();
        connMySQL.close();
        
        return idMovimientoGenerado;
    }
    
    public int insertarMovimientoCompra(Movimiento m) throws SQLException{
        String query = "{CALL insertarMovimientoEgreso(?, ?, ?, ?, ?)}";
        
        int idMovimientoGenerado = -1;
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        Connection conn = connMySQL.open();
        
        CallableStatement clblsmt = conn.prepareCall(query);
        
        clblsmt.setString(1, m.getMateriaPrima().getFechaCompra());
        clblsmt.setString(2, "Egreso");
        clblsmt.setDouble(3, m.getMateriaPrima().getPrecioCompra());
        clblsmt.setInt(4, m.getMateriaPrima().getIdMateriaPrima());
        
        clblsmt.registerOutParameter(5, Types.INTEGER);
        
        clblsmt.executeUpdate();
        
        idMovimientoGenerado = clblsmt.getInt(5);
        m.setIdMovimiento(idMovimientoGenerado);
        
        clblsmt.close();
        connMySQL.close();
        
        return idMovimientoGenerado;
    }
    
    public boolean actualizarMovimientoIngreso(Movimiento m) throws Exception{
        String query = "{CALL actualizarMovimientoIngreso(?, ?, ?, ?)}";
        boolean exito = true;
        
        try{
            ConexionMySQL connMySQL = new ConexionMySQL();
            
            Connection conn = connMySQL.open();
            
            CallableStatement clblsmt = conn.prepareCall(query);
            
            clblsmt.setString(1, m.getVenta().getFechaVenta());
            clblsmt.setString(2, "Ingreso");
            clblsmt.setDouble(3, m.getVenta().getTotal());
            clblsmt.setInt(4, m.getVenta().getIdVenta());
            
            clblsmt.executeUpdate();
            
            clblsmt.close();
            connMySQL.close();
        } catch(Exception ex){
            exito = false;
        }
        return exito;
    }
    
    public boolean actualizarMovimientoEgreso(Movimiento m) throws Exception{
        String query = "{CALL actualizarMovimientoEgreso(?, ?, ?, ?)}";
        boolean exito = true;
        
        try{
            ConexionMySQL connMySQL = new ConexionMySQL();
            
            Connection conn = connMySQL.open();
            
            CallableStatement clblsmt = conn.prepareCall(query);
            
            clblsmt.setString(1, m.getVenta().getFechaVenta());
            clblsmt.setString(2, "Egreso");
            clblsmt.setDouble(3, m.getVenta().getTotal());
            clblsmt.setInt(4, m.getMateriaPrima().getIdMateriaPrima());
            
            clblsmt.executeUpdate();
            
            clblsmt.close();
            connMySQL.close();
        } catch(Exception ex){
            exito = false;
        }
        return exito;
    }
    
    public List<Movimiento> getMovimientos(String desde, String hasta) throws SQLException{
        String query = "SELECT * FROM v_movimiento WHERE fechaMovimiento BETWEEN STR_TO_DATE(" + desde + ", '%d/%m/%Y') AND STR_TO_DATE(" + hasta + ", '%d/%m/%Y');";

        ConexionMySQL connMySQL = new ConexionMySQL();
        
        Connection conn = connMySQL.open();
        
        Statement stmt = (Statement)conn.createStatement();
        
        ResultSet rs = null;
        
        rs = stmt.executeQuery(query);
        
        List<Movimiento> m = new ArrayList<>();
        
        while(rs.next())
            m.add(fill(rs));
        
        rs.close();
        stmt.close();
        connMySQL.close();
        
        return m;
    }
    
    private Movimiento fill(ResultSet rs) throws SQLException{
        Medida me = new Medida();
        MateriaPrima mp = new MateriaPrima();
        Venta v = new Venta();
        Movimiento m = new Movimiento();
        
        me.setIdMedida(rs.getInt("idMedida"));
        me.setTipoMedida(rs.getString("tipoMedida"));
        
        mp.setIdMateriaPrima(rs.getInt("idMateriaPrima"));
        mp.setNombreMateria(rs.getString("nombreMateria"));
        mp.setFechaCompra(rs.getString("fechaCompra"));
        mp.setFechaVencimiento(rs.getString("fechaVencimiento"));
        mp.setEstatus(rs.getInt("estatus"));
        mp.setCantidadExistentes(rs.getFloat("cantidadExistentes"));
        mp.setPrecioCompra(rs.getFloat("precioCompra"));
        mp.setPorcentaje(rs.getInt("porcentaje"));
        mp.setMedida(me);
        
        v.setIdVenta(rs.getInt("idVenta"));
        v.setFechaVenta(rs.getString("fechaVenta"));
        v.setTotal(rs.getFloat("total"));
        
        m.setIdMovimiento(rs.getInt("idMovimiento"));
        m.setFechaMovimiento(rs.getString("fechaMovimiento"));
        m.setTipoMovimiento(rs.getString("tipoMovimiento"));
        m.setMonto(rs.getFloat("monto"));
        m.setVenta(v);
        m.setMateriaPrima(mp);
        
        return m;
    }
    
    public Resultado getMateriaMasComprada() throws SQLException{
        String query = "SELECT nombreMateria AS mas FROM (SELECT nombreMateria, SUM(cantidadExistentes) AS cantidad FROM materia_prima GROUP BY nombreMateria) subconsulta ORDER BY cantidad DESC LIMIT 1";

        ConexionMySQL connMySQL = new ConexionMySQL();
        
        Connection conn = connMySQL.open();
        
        Statement stmt = (Statement)conn.createStatement();
        
        ResultSet rs = null;
        
        rs = stmt.executeQuery(query);
        
        Resultado r = new Resultado();
        
        if(rs.next())
            r = fillMas(rs);
        
        rs.close();
        stmt.close();
        connMySQL.close();
        
        return r;
    }
    
    public Resultado getProductoMasVendido() throws SQLException{
        String query = "SELECT nombreProducto AS mas FROM producto p INNER JOIN detalle_venta dv ON p.idProducto = dv.idProducto GROUP BY nombreProducto ORDER BY COUNT(*) DESC LIMIT 1";

        ConexionMySQL connMySQL = new ConexionMySQL();
        
        Connection conn = connMySQL.open();
        
        Statement stmt = (Statement)conn.createStatement();
        
        ResultSet rs = null;
        
        rs = stmt.executeQuery(query);
        
        Resultado r = new Resultado();
        
        if(rs.next())
            r = fillMas(rs);
        
        rs.close();
        stmt.close();
        connMySQL.close();
        
        return r;
    }
    
    private Resultado fillMas(ResultSet rs) throws SQLException{
        Resultado r = new Resultado();
        
        r.setNombre(rs.getString("mas"));
        
        return r;
    }
}
