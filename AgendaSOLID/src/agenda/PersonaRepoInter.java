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
public interface PersonaRepoInter {
    int agregar(Persona p, Connection conn) throws SQLException;
    void actualizarNombre(int id, String nuevoNombre, Connection conn) throws SQLException;
    void eliminar(String nombre, Connection conn) throws SQLException;
    int obtenerIdPorNombre(String nombre, Connection conn) throws SQLException;
}

