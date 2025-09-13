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
public class PersonaDirecBD implements PersonaDirecRepoInter {

    @Override
    public void agregar(int personaId, int direccionId, Connection conn) throws SQLException {
        if(!existe(personaId, direccionId, conn)) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Persona_Direccion(personaId, direccionId) VALUES(?, ?)")) {
                ps.setInt(1, personaId);
                ps.setInt(2, direccionId);
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void eliminar(int personaId, int direccionId, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM Persona_Direccion WHERE personaId=? AND direccionId=?")) {
            ps.setInt(1, personaId);
            ps.setInt(2, direccionId);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean existe(int personaId, int direccionId, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM Persona_Direccion WHERE personaId=? AND direccionId=? LIMIT 1")) {
            ps.setInt(1, personaId);
            ps.setInt(2, direccionId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }
}