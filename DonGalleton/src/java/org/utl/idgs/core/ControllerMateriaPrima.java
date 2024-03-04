package org.utl.idgs.core;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.utl.idgs.connection.ConexionMySQL;
import org.utl.idgs.model.MateriaPrima;
import org.utl.idgs.model.Medida;
import org.utl.idgs.model.Movimiento;
import org.utl.idgs.model.Producto;

/**
 *
 * @author Alda
 */
public class ControllerMateriaPrima {

    public int insertarMateriaPrima(MateriaPrima mp) throws Exception {
        String sql = "{call insertarMateriaPrima(?, ?,"
                + "?, ?, "
                + "?, ?, ?, ?)}";

        int idMateriaGenerado = -1;
        
        Movimiento m = new Movimiento();
        ControllerMovimiento cm = new ControllerMovimiento();
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql);

        cstmt.setString(1, mp.getNombreMateria());
        cstmt.setString(2, mp.getFechaCompra());
        cstmt.setString(3, mp.getFechaVencimiento());
        cstmt.setDouble(4, mp.getCantidadExistentes());
        cstmt.setDouble(5, mp.getPrecioCompra());
        cstmt.setDouble(6, mp.getPorcentaje());
        cstmt.setInt(7, mp.getMedida().getIdMedida());
        cstmt.registerOutParameter(8, Types.INTEGER);
        cstmt.executeUpdate();
        idMateriaGenerado = cstmt.getInt(8);
        mp.setIdMateriaPrima(idMateriaGenerado);
        
        m.setMateriaPrima(mp);
        cm.insertarMovimientoCompra(m);

        cstmt.close();
        connMySQL.close();

        return idMateriaGenerado;
    }
    
    public void eliminarMateriaPrima(int idMateriaPrima) throws Exception {
        String query = "DELETE FROM movimiento WHERE idMateriaPrima = ?";
        String sql = "DELETE FROM materia_prima WHERE idMateriaPrima = ?;";
    
        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();
        CallableStatement cstmt1 = conn.prepareCall(query);
        
        cstmt1.setInt(1, idMateriaPrima);
        cstmt1.executeUpdate();
        
        CallableStatement cstmt = conn.prepareCall(sql);
 
        cstmt.setInt(1, idMateriaPrima);
        cstmt.executeUpdate();

        cstmt1.close();
        cstmt.close();
        connMySQL.close();
    }

    public void actualizarMateriaPrima(MateriaPrima mp) throws Exception {
        String sql = "{call actualizarMateriaPrima(?, ?,"
                + "?, ?, "
                + "?, ?, ?, ?)}";

        Movimiento m = new Movimiento();
        ControllerMovimiento cm = new ControllerMovimiento();
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql);

        cstmt.setInt(1, mp.getIdMateriaPrima());
        cstmt.setString(2, mp.getNombreMateria());
        cstmt.setString(3, mp.getFechaCompra());
        cstmt.setString(4, mp.getFechaVencimiento());
        cstmt.setDouble(5, mp.getCantidadExistentes());
        cstmt.setDouble(6, mp.getPrecioCompra());
        cstmt.setDouble(7, mp.getPorcentaje());
        cstmt.setInt(8, mp.getMedida().getIdMedida());

        cstmt.executeUpdate();
        
        m.setMateriaPrima(mp);
        cm.actualizarMovimientoEgreso(m);

        cstmt.close();
        connMySQL.close();
    }

    public MateriaPrima getProducto(int filtro) throws Exception {
        System.out.println("FILTRO  " + filtro);
        String sql = "SELECT * FROM v_materia_prima WHERE idMateriaPrima = '" + filtro + "';";

        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        ResultSet rs = pstmt.executeQuery();
        MateriaPrima materia = new MateriaPrima();

        while (rs.next()) {
            materia = fill(rs);
        }
        System.out.println("CONTROLLER: " + materia);

        rs.close();
        pstmt.close();
        connMySQL.close();

        return materia;
    }

    public List<MateriaPrima> getAll(String filtro) throws Exception {
//        String sql = "SELECT * FROM v_materia_prima WHERE estatus = 1;";
          String sql = "SELECT * FROM v_materia_prima";

        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        ResultSet rs = pstmt.executeQuery();
        List<MateriaPrima> material = new ArrayList<>();

        while (rs.next()) {
            material.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return material;
    }

    private MateriaPrima fill(ResultSet rs) throws Exception {
        MateriaPrima mp = new MateriaPrima();
        Medida m = new Medida();

        m.setIdMedida(rs.getInt("idMedida"));
        m.setTipoMedida(rs.getString("tipoMedida"));
        mp.setMedida(m);
        mp.setIdMateriaPrima(rs.getInt("idMateriaPrima"));
        mp.setNombreMateria(rs.getString("nombreMateria"));
        mp.setFechaCompra(rs.getString("fechaCompra"));
        mp.setFechaVencimiento(rs.getString("fechaVencimiento"));
        mp.setCantidadExistentes(rs.getFloat("cantidadExistentes"));
        mp.setPrecioCompra(rs.getFloat("precioCompra"));
        mp.setPorcentaje(rs.getInt("porcentaje"));

        return mp;
    }
}
