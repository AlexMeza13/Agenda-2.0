/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package agenda;

import java.sql.*;

/**
 *
 * @author alexm
 */
public interface TelefonoRepoInter {
    void agregar(Telefono t, Connection conn) throws SQLException;
    void actualizar(int idTelefono, String nuevoTelefono, int personaId, Connection conn) throws SQLException;
    void eliminar(int personaId, String telefono, Connection conn) throws SQLException;
}
