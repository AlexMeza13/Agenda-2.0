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
public class TelefonoBD implements TelefonoRepoInter {

    @Override
    public void agregar(Telefono t, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Telefonos(personaId, telefono) VALUES(?, ?)")) {
            ps.setInt(1, t.getPersonaId());
            ps.setString(2, t.getTelefono());
            ps.executeUpdate();
        }
    }

    @Override
    public void actualizar(int idTelefono, String nuevoTelefono, int personaId, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE Telefonos SET telefono=? WHERE id=? AND personaId=?")) {
            ps.setString(1, nuevoTelefono);
            ps.setInt(2, idTelefono);
            ps.setInt(3, personaId);
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int personaId, String telefono, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM Telefonos WHERE personaId=? AND telefono=?")) {
            ps.setInt(1, personaId);
            ps.setString(2, telefono);
            ps.executeUpdate();
        }
    }
}
