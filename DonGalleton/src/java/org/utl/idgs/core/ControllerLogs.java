/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.idgs.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.utl.idgs.connection.ConexionMySQL;
import org.utl.idgs.model.Logs;

/**
 *
 * @author Usuario
 */
public class ControllerLogs {
    public List<Logs> getAll(String filtro) throws Exception {
          String sql = "SELECT * FROM logsUser";

        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        ResultSet rs = pstmt.executeQuery();
        List<Logs> logs = new ArrayList<>();

        while (rs.next()) {
            logs.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return logs;
    }
    
    private Logs fill(ResultSet rs) throws SQLException{
        Logs l = new Logs();
        
        l.setId(rs.getInt("id"));
        l.setUsuario(rs.getString("usuario"));
        l.setProcedimiento(rs.getString("procedimiento"));
        l.setFecha(rs.getString("fecha"));
        
        return l;
    }
}
