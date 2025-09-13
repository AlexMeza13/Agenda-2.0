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
public interface DireccionRepoInter {
    int agregar(Direccion d, Connection conn) throws SQLException;
    int obtenerIdPorNombre(String direccion, Connection conn) throws SQLException;
    void eliminarSiNoUsada(int direccionId, Connection conn) throws SQLException;
}