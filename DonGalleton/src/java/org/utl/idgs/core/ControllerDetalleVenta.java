package org.utl.idgs.core;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.utl.idgs.connection.ConexionMySQL;
import org.utl.idgs.model.DetalleVenta;
import org.utl.idgs.model.Medida;
import org.utl.idgs.model.Producto;
import org.utl.idgs.model.Venta;

/**
 *
 * @author Alda
 */
public class ControllerDetalleVenta {

    public int insertarDetalleVenta(DetalleVenta dv) throws Exception {
        String sql = "{call insertarDetalleVenta(?, ?,"
                + "?, ?, "
                + "?, ?)}";

        int idDetalleVentaGenerado = -1;
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql);

        cstmt.setDouble(1, dv.getCantidad());
        cstmt.setDouble(2, dv.getSubtotal());
        cstmt.setInt(3, dv.getVenta().getIdVenta());
        cstmt.setInt(4, dv.getProducto().getIdProducto());
        cstmt.setInt(5, dv.getMedida().getIdMedida());
        cstmt.registerOutParameter(6, Types.INTEGER);
        cstmt.executeUpdate();
        idDetalleVentaGenerado = cstmt.getInt(6);
        dv.setIdDetalleVenta(idDetalleVentaGenerado);

        actualizarExistenciasProducto(dv);
         
        cstmt.close();
        connMySQL.close();

        return idDetalleVentaGenerado;
    }

    public void actualizarDetalleVenta(DetalleVenta dv) throws Exception {
        String sql = "{call actualizarDetalleVenta(?, ?,"
                + "?, ?, "
                + "?, ?)}";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql);

        cstmt.setDouble(2, dv.getCantidad());
        cstmt.setDouble(3, dv.getSubtotal());
        cstmt.setInt(4, dv.getVenta().getIdVenta());
        cstmt.setInt(5, dv.getProducto().getIdProducto());
        cstmt.setInt(6, dv.getMedida().getIdMedida());

        cstmt.setInt(1, dv.getIdDetalleVenta());

        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }

    public List<DetalleVenta> getAll(String filtro) throws Exception {
        String sql = "SELECT * FROM v_detalle_venta;";

        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        ResultSet rs = pstmt.executeQuery();
        List<DetalleVenta> dventas = new ArrayList<>();

        while (rs.next()) {
            dventas.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return dventas;
    }

    private DetalleVenta fill(ResultSet rs) throws Exception {
        DetalleVenta dv = new DetalleVenta();
        Venta v = new Venta();
        Producto p = new Producto();
        Medida m = new Medida();

        m.setIdMedida(rs.getInt("idMedida"));
        m.setTipoMedida(rs.getString("tipoMedida"));
        dv.setMedida(m);

        p.setIdProducto(rs.getInt("idProducto"));
        p.setNombreProducto(rs.getString("nombreProducto"));
        p.setCantidadExistentes(rs.getDouble("cantidadExistentes"));
        p.setPrecioVenta(rs.getDouble("precioVenta"));
        p.setPrecioProduccion(rs.getDouble("precioProduccion"));
        p.setFotografia(rs.getString("fotografia"));
        dv.setProducto(p);

        v.setIdVenta(rs.getInt("idVenta"));
        v.setFechaVenta(rs.getString("fechaVenta"));
        v.setTotal((float) rs.getDouble("total"));
        dv.setVenta(v);

        dv.setIdDetalleVenta(rs.getInt("idDetalleVenta"));
        dv.setCantidad((float) rs.getDouble("cantidad"));
        dv.setSubtotal((float) rs.getDouble("subtotal"));

        return dv;
    }
    // Método para actualizar las existencias de un producto en la base de datos y restar la cantidad vendida

        public void actualizarExistenciasProducto(DetalleVenta dv) throws Exception {
            Producto producto = obtenerProductoPorId(dv.getProducto().getIdProducto());

            if (producto != null) {
                double cantidadExistente = producto.getCantidadExistentes();
                double cantidadVendida = dv.getCantidad();
                double nuevaCantidad = cantidadExistente - cantidadVendida;

                if (nuevaCantidad >= 0) {
                    producto.setCantidadExistentes(nuevaCantidad);
                    updateExistenciasEnBaseDeDatos(producto);
                } else {
                    throw new Exception("No hay suficientes existencias para la venta.");
                }
            } else {
                throw new Exception("Producto no encontrado.");
            }
        }

    // Método para obtener un producto por su ID
        public Producto obtenerProductoPorId(int idProducto) throws Exception {
            String sql = "SELECT * FROM producto WHERE idProducto = ?";

            ConexionMySQL connMySQL = new ConexionMySQL();
            Connection conn = connMySQL.open();

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idProducto);

            ResultSet rs = pstmt.executeQuery();
            Producto producto = null;

            if (rs.next()) {
                producto = fillProducto(rs);
            }

            rs.close();
            pstmt.close();
            connMySQL.close();

            return producto;
        }

    // Método para llenar un objeto Producto desde un ResultSet
        private Producto fillProducto(ResultSet rs) throws Exception {
            Producto p = new Producto();

            p.setIdProducto(rs.getInt("idProducto"));
            p.setNombreProducto(rs.getString("nombreProducto"));
            p.setCantidadExistentes(rs.getDouble("cantidadExistentes"));
            p.setPrecioVenta(rs.getDouble("precioVenta"));
            p.setPrecioProduccion(rs.getDouble("precioProduccion"));
            p.setFotografia(rs.getString("fotografia"));

            return p;
        }

    // Método para actualizar las existencias de un producto en la base de datos
        public void updateExistenciasEnBaseDeDatos(Producto producto) throws Exception {
            String sql = "UPDATE producto SET cantidadExistentes = ? WHERE idProducto = ?";

            ConexionMySQL connMySQL = new ConexionMySQL();
            Connection conn = connMySQL.open();

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, producto.getCantidadExistentes());
            pstmt.setInt(2, producto.getIdProducto());

            pstmt.executeUpdate();

            pstmt.close();
            connMySQL.close();
        }

}
