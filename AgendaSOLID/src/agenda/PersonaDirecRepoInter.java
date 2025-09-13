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
public interface PersonaDirecRepoInter {
    void agregar(int personaId, int direccionId, Connection conn) throws SQLException;
    void eliminar(int personaId, int direccionId, Connection conn) throws SQLException;
    boolean existe(int personaId, int direccionId, Connection conn) throws SQLException;
}
