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
public class PersonaBD implements PersonaRepoInter {

 @Override
    public int agregar(Persona p, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Personas(nombre) VALUES(?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }

    @Override
    public void actualizarNombre(int id, String nuevoNombre, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE Personas SET nombre=? WHERE id=?")) {
            ps.setString(1, nuevoNombre);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(String nombre, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM Personas WHERE nombre=?")) {
            ps.setString(1, nombre);
            ps.executeUpdate();
        }
    }

    @Override
    public int obtenerIdPorNombre(String nombre, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id FROM Personas WHERE nombre=? LIMIT 1")) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        return -1;
    }
}
