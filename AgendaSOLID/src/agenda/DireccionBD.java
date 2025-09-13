/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agenda;

import java.sql.*;

/**
 *
 * @author alexm
 */
public class DireccionBD implements DireccionRepoInter {

    @Override
    public int agregar(Direccion d, Connection conn) throws SQLException {
        int existingId = obtenerIdPorNombre(d.getDireccion(), conn);
        if(existingId != -1) return existingId;

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Direcciones(direccion) VALUES(?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getDireccion());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }

    @Override
    public int obtenerIdPorNombre(String direccion, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id FROM Direcciones WHERE direccion=? LIMIT 1")) {
            ps.setString(1, direccion);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        return -1;
    }

    @Override
    public void eliminarSiNoUsada(int direccionId, Connection conn) throws SQLException {
        String checkSql = "SELECT COUNT(*) AS cnt FROM Persona_Direccion WHERE direccionId=?";
        try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
            ps.setInt(1, direccionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt("cnt") == 0) {
                try (PreparedStatement psDel = conn.prepareStatement(
                        "DELETE FROM Direcciones WHERE id=?")) {
                    psDel.setInt(1, direccionId);
                    psDel.executeUpdate();
                }
            }
        }
    }
}
