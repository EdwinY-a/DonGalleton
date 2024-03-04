
package org.utl.idgs.core;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.utl.idgs.connection.ConexionMySQL;
import org.utl.idgs.model.CrearProducto;
import org.utl.idgs.model.MateriaPrima;
import org.utl.idgs.model.Medida;
import org.utl.idgs.model.Producto;

/**
 *
 * @author jorgemorales
 */
public class ControllerProducto {
    public int insertarProducto(Producto p) throws Exception {
        String sql = "{call insertarProducto(?, ?,"
                                            + "?, ?, "
                                            + "?, ?, ?)}";
       
        int idProductoGenerado = -1;
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql);
        
        cstmt.setString(1, p.getNombreProducto());
        cstmt.setDouble(2, p.getCantidadExistentes());
        cstmt.setDouble(3, p.getPrecioVenta());
        cstmt.setDouble(4, p.getPrecioProduccion());
        cstmt.setInt(5, p.getMedida().getIdMedida());
        cstmt.setString(6, p.getFotografia());
        cstmt.registerOutParameter(7, Types.INTEGER);
        cstmt.executeUpdate();
        idProductoGenerado = cstmt.getInt(7);
        p.setIdProducto(idProductoGenerado);

        cstmt.close();
        connMySQL.close();

        return idProductoGenerado;
    }
    
    public int insertarCrearProducto(CrearProducto p) throws Exception {
        String sql = "{call insertarCrearProducto(?, ?,"
                                                + "?, ?, "
                                                + "?)}";
       
        int idProductoGenerado = -1;
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql);
        
        cstmt.setFloat(1, p.getPorcion());
        cstmt.setDouble(2, p.getProducto().getIdProducto());
        cstmt.setDouble(3, p.getMedida().getIdMedida());
        cstmt.setDouble(4, p.getMateriaPrima().getIdMateriaPrima());
        cstmt.registerOutParameter(5, Types.INTEGER);
        cstmt.executeUpdate();
        idProductoGenerado = cstmt.getInt(5);
        p.setIdCrearProducto(idProductoGenerado);

        cstmt.close();
        connMySQL.close();

        return idProductoGenerado;
    }
    
    public void actualizarProducto(Producto p) throws Exception {
        String sql = "{call actualizarProducto(?, ?,"
                                            + "?, ?, "
                                            + "?, ?, ?)}"; 

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql); 
        
        cstmt.setString(2, p.getNombreProducto());
        cstmt.setDouble(3, p.getCantidadExistentes());
        cstmt.setDouble(4, p.getPrecioVenta());
        cstmt.setDouble(5, p.getPrecioProduccion());
        cstmt.setInt(6, p.getMedida().getIdMedida());
        cstmt.setString(7, p.getFotografia());
        
        cstmt.setInt(1, p.getIdProducto());

        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }
    
    
    public void actualizarMateriaPrima(MateriaPrima mp) throws Exception {
        String sql = "{call actualizarMateriaPrima(?, ?,"
                                                    + "?, ?, "
                                                    + "?, ?, ?, ?)}"; 

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql); 
        
        cstmt.setInt(1, mp.getIdMateriaPrima());
        cstmt.setString(2, mp.getNombreMateria());
        cstmt.setString(3, mp.getFechaCompra());
        cstmt.setString(4, mp.getFechaVencimiento());
        cstmt.setDouble(5, mp.getCantidadExistentes());
        cstmt.setDouble(6,mp.getPrecioCompra());
        cstmt.setInt(7, mp.getPorcentaje());
        cstmt.setInt(8, mp.getMedida().getIdMedida());      

        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }
        
    public void eliminarProducto(int idProducto) throws Exception {
        
    String sql1 = "DELETE FROM crear_producto WHERE idProducto="+idProducto;
    String sql = "DELETE FROM producto WHERE idProducto="+idProducto;
    
    ConexionMySQL connMySQL = new ConexionMySQL();

    Connection conn = connMySQL.open();

    PreparedStatement pstm1 = conn.prepareStatement(sql1);
    PreparedStatement pstm = conn.prepareStatement(sql);

     pstm1.executeUpdate();
     pstm.executeUpdate();

    pstm.close();
    pstm1.close();
    connMySQL.close();

    }
    
    public List<Producto> getAll(String filtro) throws Exception {
        String sql = "SELECT * FROM v_producto;" ;

        ConexionMySQL connMySQL = new ConexionMySQL(); 

        Connection conn = connMySQL.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        ResultSet rs = pstmt.executeQuery();
        List<Producto> productos = new ArrayList<>();

        while (rs.next()) {
            productos.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return productos;
    }
    
    public Producto getProducto(int filtro) throws Exception {
        String sql = "SELECT * FROM v_producto WHERE idProducto ="+filtro+";" ;

        ConexionMySQL connMySQL = new ConexionMySQL(); 

        Connection conn = connMySQL.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        ResultSet rs = pstmt.executeQuery();
        Producto productos = new Producto();

        while (rs.next()) {
            productos = fill(rs);
        }
        System.out.println("CONTROLLER: "+productos);

        rs.close();
        pstmt.close();
        connMySQL.close();

        return productos;
    }
    
    public List<CrearProducto> getProductoCreado(int filtro) throws Exception {
        String sql = "SELECT * FROM v_crear_producto WHERE idProducto ="+filtro+";" ;

        ConexionMySQL connMySQL = new ConexionMySQL(); 

        Connection conn = connMySQL.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        ResultSet rs = pstmt.executeQuery();
        List<CrearProducto> productosCreados = new ArrayList<>();

        while (rs.next()) {
            productosCreados.add(fillCrearProductos(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return productosCreados;
    }
    
    public List<MateriaPrima> getAllIngredientes(String filtro) throws Exception {
        String sql = "SELECT * FROM v_materia_prima;" ;

        ConexionMySQL connMySQL = new ConexionMySQL(); 

        Connection conn = connMySQL.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        ResultSet rs = pstmt.executeQuery();
        List<MateriaPrima> materias = new ArrayList<>();

        while (rs.next()) {
            materias.add(fillMateriaPrima(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return materias;
    }
    
    private Producto fill(ResultSet rs) throws Exception {
        Producto p = new Producto();
        Medida m = new Medida();
        
        m.setIdMedida(rs.getInt("idMedida"));
        m.setTipoMedida(rs.getString("tipoMedida"));
        p.setMedida(m);
        p.setIdProducto(rs.getInt("idProducto"));
        p.setNombreProducto(rs.getString("nombreProducto"));
        p.setCantidadExistentes(rs.getDouble("cantidadExistentes"));
        p.setPrecioVenta(rs.getDouble("precioVenta"));
        p.setPrecioProduccion(rs.getDouble("precioProduccion"));
        p.setFotografia(rs.getString("fotografia"));   
       
        return p;
    }   
            
    private MateriaPrima fillMateriaPrima(ResultSet rs) throws Exception {
        MateriaPrima mp = new MateriaPrima();
        Medida m = new Medida();
        
        m.setIdMedida(rs.getInt("idMedida"));
        m.setTipoMedida(rs.getString("tipoMedida"));
        mp.setMedida(m);
        
        // lenar materiaPrima
        mp.setIdMateriaPrima(rs.getInt("idMateriaPrima"));
        mp.setNombreMateria(rs.getString("nombreMateria"));
        mp.setFechaCompra(rs.getString("fechaCompra"));
        mp.setFechaVencimiento(rs.getString("fechaVencimiento"));
        mp.setEstatus(rs.getInt("estatus"));
        mp.setCantidadExistentes(rs.getFloat("cantidadExistentes"));
        mp.setPrecioCompra(rs.getFloat("precioCompra"));
        mp.setPorcentaje(rs.getInt("porcentaje"));            
       
        return mp;
    } 
    
    private CrearProducto fillCrearProductos(ResultSet rs) throws Exception {
        CrearProducto cp = new CrearProducto();
        Producto p = new Producto();
        Medida m = new Medida();
        MateriaPrima mp = new MateriaPrima();
        
        cp.setIdCrearProducto(rs.getInt("idCrearProducto"));
        cp.setPorcion(rs.getFloat("porcion"));
        
        p.setIdProducto(rs.getInt("idProducto"));
        cp.setProducto(p);
 
        m.setIdMedida(rs.getInt("idMedida"));
        cp.setMedida(m);
        
        mp.setIdMateriaPrima(rs.getInt("idMateriaPrima"));
        mp.setCantidadExistentes(rs.getInt("cantidadExistentesMateriaPrima"));
        mp.setNombreMateria(rs.getString("nombreMateria"));
        mp.setFechaCompra(rs.getString("fechaCompra"));
        mp.setFechaVencimiento(rs.getString("fechaVencimiento"));
        mp.setPrecioCompra(rs.getFloat("precioCompra"));
        mp.setPorcentaje(rs.getInt("porcentaje"));
        m.setIdMedida(rs.getInt("idMedidaMateriaPrima"));
        cp.setMateriaPrima(mp);
        
//        // lenar producto
//        p.setIdProducto(rs.getInt("idProducto"));
//        p.setNombreProducto(rs.getString("nombreProducto"));
//        p.setCantidadExistentes(rs.getDouble("cantidadExistentesProductos"));
//        p.setPrecioVenta(rs.getDouble("precioVenta"));
//        p.setPrecioProduccion(rs.getDouble("precioProduccion"));
//        m.setIdMedida(rs.getInt("idMedidaProducto"));
//        p.setMedida(m);
//        p.setFotografia(rs.getString("fotografia"));
//        cp.setProducto(p);
//        
//        // lenar medida
//        m.setIdMedida(rs.getInt("idMedida"));
//        m.setTipoMedida(rs.getString("tipoMedidaProducto"));
//        cp.setMedida(m);
//        
//        // lenar materiaPrima
//        mp.setIdMateriaPrima(rs.getInt("idMateriaPrima"));
//        mp.setNombreMateria(rs.getString("nombreMateria"));
//        mp.setFechaCompra(rs.getString("fechaCompra"));
//        mp.setFechaVencimiento(rs.getString("fechaVencimiento"));
//        mp.setFechaCompra(rs.getString("fechaCompra"));
//        mp.setEstatus(rs.getInt("estatus"));
//        mp.setCantidadExistentes(rs.getFloat("cantidadExistentesMateriaPrima"));
//        mp.setPrecioCompra(rs.getFloat("precioCompra"));
//        mp.setPorcentaje(rs.getInt("porcentaje"));
//        m.setIdMedida(rs.getInt("idMedidaMateriaPrima"));
//        m.setTipoMedida(rs.getString("tipoMedidaMateriaPrima"));
//        cp.setMateriaPrima(mp);
                
        return cp;
    }
}
