
package agenda;

import java.sql.*;

/**
 *
 * @author alexm
 */


public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/agenda";
    private static final String USER = "root";
    private static final String PASSWORD = "alexmeza";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

