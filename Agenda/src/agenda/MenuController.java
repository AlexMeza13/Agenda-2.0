package agenda;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author alexm
 */
public class MenuController implements Initializable {

    @FXML
    private TextField fieldNombreA;

    @FXML
    private TextField fieldTelefonoA;

    @FXML
    private TextField fieldDireccionA;

    @FXML
    private TextField fieldNombreD;

    @FXML
    private TextField fieldTelefonoD;

    @FXML
    private TextField fieldDireccionD;

    @FXML
    private TextField fieldIdPersona;

    @FXML
    private TextField fieldNombreUp;

    @FXML
    private TextField fieldTelefonoUp;

    @FXML
    private TextField fieldIdTelefono;

    @FXML
    private TextField fieldDireccionUp;

    @FXML
    private Button botonAgregar;

    @FXML
    private Button botonEliminar;

    @FXML
    private Button botonActualizar;

    @FXML
    private Button botonRegistro;

    @FXML
    private Button botonSqlAdd;

    @FXML
    private Button botonSqlDel;

    @FXML
    private Button botonSqlRP;

    @FXML
    private Button botonSqlRT;

    @FXML
    private Button botonSqlUp;

    @FXML
    private void evenButtonRP(ActionEvent event) {
        try (Connection conn = Conexion.getConnection()) {
            String query = """
            SELECT p.id AS PersonaID, p.nombre AS Nombre, d.id AS DireccionID, d.direccion AS Direccion
            FROM Personas p
            LEFT JOIN Persona_Direccion pd ON p.id = pd.personaId
            LEFT JOIN Direcciones d ON pd.direccionId = d.id
        """;

            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            TableView<ObservableList> tableView = new TableView<>();

            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            for (int i = 1; i <= colCount; i++) {
                final int j = i;
                TableColumn<ObservableList, String> col = new TableColumn<>(rsmd.getColumnName(i));
                col.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j - 1).toString()));
                tableView.getColumns().add(col);
            }

            ObservableList<ObservableList> data = FXCollections.observableArrayList();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= colCount; i++) {
                    row.add(rs.getString(i) != null ? rs.getString(i) : "");
                }
                data.add(row);
            }
            tableView.setItems(data);

            Stage stage = new Stage();
            stage.setTitle("Tabla Persona con Direcciones");
            Scene scene = new Scene(new VBox(tableView), 600, 400);
            stage.setScene(scene);
            stage.show();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void evenButtonRT(ActionEvent event) {
        try (Connection conn = Conexion.getConnection()) {
            String query = "SELECT * FROM Telefonos";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            TableView tableView = new TableView();
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            for (int i = 1; i <= colCount; i++) {
                final int j = i;
                TableColumn<ObservableList, String> col = new TableColumn<>(rsmd.getColumnName(i));
                col.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j - 1).toString()));
                tableView.getColumns().add(col);
            }

            ObservableList<ObservableList> data = FXCollections.observableArrayList();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= colCount; i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
            tableView.setItems(data);

            Stage stage = new Stage();
            stage.setTitle("Tabla Telefonos");
            Scene scene = new Scene(new VBox(tableView), 400, 300);
            stage.setScene(scene);
            stage.show();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void evenButtonUp(ActionEvent event) {
        try (Connection conn = Conexion.getConnection()) {

            String idPersona = fieldIdPersona.getText().trim();
            String nombre = fieldNombreUp.getText().trim();
            String telefono = fieldTelefonoUp.getText().trim();
            String idTelefono = fieldIdTelefono.getText().trim();

            PreparedStatement stmt = null;

            // actualizar nombre usando idPersona
            if (!idPersona.isEmpty() && !nombre.isEmpty()
                    && telefono.isEmpty() && idTelefono.isEmpty()) {
                String sql = "UPDATE Personas SET nombre = ? WHERE id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, nombre);
                stmt.setInt(2, Integer.parseInt(idPersona));
            } // actualizar telefono usando nombre y idTelefono
            else if (!nombre.isEmpty() && !idTelefono.isEmpty() && !telefono.isEmpty()
                    && idPersona.isEmpty()) {
                String sql = "UPDATE Telefonos SET telefono = ? WHERE id = ? "
                        + "AND personaId = (SELECT id FROM Personas WHERE nombre = ?)";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, telefono);
                stmt.setInt(2, Integer.parseInt(idTelefono));
                stmt.setString(3, nombre);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Datos insuficientes");
                alert.setHeaderText(null);
                alert.setContentText("Debes llenar los campos correctamente");
                alert.showAndWait();
                return;
            }

            int rows = stmt.executeUpdate();
            stmt.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Resultado");
            alert.setHeaderText(null);
            if (rows > 0) {
                alert.setContentText("Registro actualizado correctamente");
            } else {
                alert.setContentText("No se encontró el registro");
            }
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error al actualizar: " + e.getMessage());
            alert.showAndWait();
        }

        fieldIdPersona.clear();
        fieldNombreUp.clear();
        fieldTelefonoUp.clear();
        fieldIdTelefono.clear();
    }

    @FXML
    private void evenButtonAgregar(ActionEvent event) {
        cambiarVentana(event, "Altas.fxml");
    }

    @FXML
    private void evenButtonEliminar(ActionEvent event) {
        cambiarVentana(event, "Bajas.fxml");
    }

    @FXML
    private void evenButtonActualizar(ActionEvent event) {
        cambiarVentana(event, "Actualizar.fxml");
    }

    @FXML
    private void evenButtonRegistro(ActionEvent event) {
        cambiarVentana(event, "Registros.fxml");
    }

    @FXML
    private void evenButtonAdd(ActionEvent event) {
        String nombre = fieldNombreA.getText().trim();
        String direccion = fieldDireccionA.getText().trim();
        String telefono = fieldTelefonoA.getText().trim();

        boolean hasNombre = !nombre.isEmpty();
        boolean hasDireccion = !direccion.isEmpty();
        boolean hasTelefono = !telefono.isEmpty();

        if (!hasNombre) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("El nombre es obligatorio.");
            alert.showAndWait();
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // nueva persona completa
                if (hasNombre && hasDireccion && hasTelefono) {
                    String insertPersona = "INSERT INTO Personas (nombre) VALUES (?)";
                    PreparedStatement psPersona = conn.prepareStatement(insertPersona, Statement.RETURN_GENERATED_KEYS);
                    psPersona.setString(1, nombre);
                    psPersona.executeUpdate();
                    ResultSet rsPersona = psPersona.getGeneratedKeys();
                    int personaId = -1;
                    if (rsPersona.next()) {
                        personaId = rsPersona.getInt(1);
                    }

                    int direccionId = -1;
                    String findDireccion = "SELECT id FROM Direcciones WHERE direccion = ? LIMIT 1";
                    PreparedStatement psFindDir = conn.prepareStatement(findDireccion);
                    psFindDir.setString(1, direccion);
                    ResultSet rsDir = psFindDir.executeQuery();
                    if (rsDir.next()) {
                        direccionId = rsDir.getInt("id");
                    } else {
                        String insertDir = "INSERT INTO Direcciones (direccion) VALUES (?)";
                        PreparedStatement psDir = conn.prepareStatement(insertDir, Statement.RETURN_GENERATED_KEYS);
                        psDir.setString(1, direccion);
                        psDir.executeUpdate();
                        ResultSet rsNewDir = psDir.getGeneratedKeys();
                        if (rsNewDir.next()) {
                            direccionId = rsNewDir.getInt(1);
                        }
                    }

                    String insertPD = "INSERT INTO Persona_Direccion (personaId, direccionId) VALUES (?, ?)";
                    PreparedStatement psPD = conn.prepareStatement(insertPD);
                    psPD.setInt(1, personaId);
                    psPD.setInt(2, direccionId);
                    psPD.executeUpdate();

                    String insertTel = "INSERT INTO Telefonos (personaId, telefono) VALUES (?, ?)";
                    PreparedStatement psTel = conn.prepareStatement(insertTel);
                    psTel.setInt(1, personaId);
                    psTel.setString(2, telefono);
                    psTel.executeUpdate();

                    conn.commit();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Resultado");
                    alert.setHeaderText(null);
                    alert.setContentText("Persona creada con su dirección y teléfono.");
                    alert.showAndWait();
                } // agregar telefono a persona existente
                else if (hasNombre && hasTelefono && !hasDireccion) {
                    String sqlFindPersona = "SELECT id FROM Personas WHERE nombre = ? LIMIT 1";
                    PreparedStatement psBuscar = conn.prepareStatement(sqlFindPersona);
                    psBuscar.setString(1, nombre);
                    ResultSet rs = psBuscar.executeQuery();

                    if (rs.next()) {
                        int personaId = rs.getInt("id");
                        String insertTel = "INSERT INTO Telefonos (personaId, telefono) VALUES (?, ?)";
                        PreparedStatement psTel = conn.prepareStatement(insertTel);
                        psTel.setInt(1, personaId);
                        psTel.setString(2, telefono);
                        psTel.executeUpdate();

                        conn.commit();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Resultado");
                        alert.setHeaderText(null);
                        alert.setContentText("Teléfono agregado a \"" + nombre + "\".");
                        alert.showAndWait();
                    } else {
                        conn.rollback();
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Aviso");
                        alert.setHeaderText(null);
                        alert.setContentText("No se encontró persona con ese nombre.");
                        alert.showAndWait();
                    }
                } // agregar direccion a persona existente
                else if (hasNombre && !hasTelefono && hasDireccion) {
                    String sqlFindPersona = "SELECT id FROM Personas WHERE nombre = ? LIMIT 1";
                    PreparedStatement psBuscar = conn.prepareStatement(sqlFindPersona);
                    psBuscar.setString(1, nombre);
                    ResultSet rs = psBuscar.executeQuery();

                    if (rs.next()) {
                        int personaId = rs.getInt("id");

                        int direccionId = -1;
                        String findDireccion = "SELECT id FROM Direcciones WHERE direccion = ? LIMIT 1";
                        PreparedStatement psFindDir = conn.prepareStatement(findDireccion);
                        psFindDir.setString(1, direccion);
                        ResultSet rsDir = psFindDir.executeQuery();
                        if (rsDir.next()) {
                            direccionId = rsDir.getInt("id");
                        } else {
                            String insertDir = "INSERT INTO Direcciones (direccion) VALUES (?)";
                            PreparedStatement psDir = conn.prepareStatement(insertDir, Statement.RETURN_GENERATED_KEYS);
                            psDir.setString(1, direccion);
                            psDir.executeUpdate();
                            ResultSet rsNewDir = psDir.getGeneratedKeys();
                            if (rsNewDir.next()) {
                                direccionId = rsNewDir.getInt(1);
                            }
                        }

                        String checkPD = "SELECT 1 FROM Persona_Direccion WHERE personaId = ? AND direccionId = ? LIMIT 1";
                        PreparedStatement psCheck = conn.prepareStatement(checkPD);
                        psCheck.setInt(1, personaId);
                        psCheck.setInt(2, direccionId);
                        ResultSet rsCheck = psCheck.executeQuery();

                        if (!rsCheck.next()) {
                            String insertPD = "INSERT INTO Persona_Direccion (personaId, direccionId) VALUES (?, ?)";
                            PreparedStatement psPD = conn.prepareStatement(insertPD);
                            psPD.setInt(1, personaId);
                            psPD.setInt(2, direccionId);
                            psPD.executeUpdate();

                            conn.commit();
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Resultado");
                            alert.setHeaderText(null);
                            alert.setContentText("Dirección agregada a \"" + nombre + "\".");
                            alert.showAndWait();
                        } else {
                            conn.rollback();
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Resultado");
                            alert.setHeaderText(null);
                            alert.setContentText("La persona ya tenía esa dirección.");
                            alert.showAndWait();
                        }
                    } else {
                        conn.rollback();
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Aviso");
                        alert.setHeaderText(null);
                        alert.setContentText("No se encontró persona con ese nombre.");
                        alert.showAndWait();
                    }
                } else {
                    conn.rollback();
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Aviso");
                    alert.setHeaderText(null);
                    alert.setContentText("Combinación inválida. Solo se permite:\n"
                            + "1) nombre + dirección + teléfono (nueva persona)\n"
                            + "2) nombre + teléfono (agregar teléfono)\n"
                            + "3) nombre + dirección (agregar dirección)");
                    alert.showAndWait();
                }

            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error al procesar: " + e.getMessage());
            alert.showAndWait();
        }

        fieldNombreA.clear();
        fieldDireccionA.clear();
        fieldTelefonoA.clear();
    }

    @FXML
    private void evenButtonDel(ActionEvent event) {
        String nombre = fieldNombreD.getText().trim();
        String telefono = fieldTelefonoD.getText().trim();
        String direccion = fieldDireccionD.getText().trim();

        if (nombre.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Debes escribir al menos el nombre.");
            alert.showAndWait();
            return;
        }

        try (Connection conn = Conexion.getConnection()) {

            if (telefono.isEmpty() && direccion.isEmpty()) {
                //eliminar persona por completo
                String sql = "DELETE FROM Personas WHERE nombre = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nombre);
                int rows = ps.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Resultado");
                alert.setHeaderText(null);

                if (rows > 0) {
                    alert.setContentText("Persona y sus datos eliminados.");
                } else {
                    alert.setContentText("No se encontró persona con ese nombre.");
                }
                alert.showAndWait();

            } else if (!telefono.isEmpty()) {
                // eliminar telefono 
                String sql = "DELETE FROM Telefonos WHERE personaId = "
                        + "(SELECT id FROM Personas WHERE nombre = ?) AND telefono = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nombre);
                ps.setString(2, telefono);
                int rows = ps.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Resultado");
                alert.setHeaderText(null);

                if (rows > 0) {
                    alert.setContentText("Teléfono eliminado.");
                } else {
                    alert.setContentText("No se encontró el número a eliminar.");
                }
                alert.showAndWait();

            } else if (!direccion.isEmpty()) {
                // eliminar direccion
                conn.setAutoCommit(false);

                try {
                    String sqlPersona = "SELECT id FROM Personas WHERE nombre = ?";
                    PreparedStatement psPersona = conn.prepareStatement(sqlPersona);
                    psPersona.setString(1, nombre);
                    ResultSet rs = psPersona.executeQuery();

                    if (!rs.next()) {
                        throw new SQLException("No existe persona con ese nombre.");
                    }
                    int personaId = rs.getInt("id");

                    String sqlDir = "SELECT id FROM Direcciones WHERE direccion = ?";
                    PreparedStatement psDir = conn.prepareStatement(sqlDir);
                    psDir.setString(1, direccion);
                    ResultSet rsDir = psDir.executeQuery();

                    if (!rsDir.next()) {
                        throw new SQLException("No existe esa dirección en la base de datos.");
                    }
                    int direccionId = rsDir.getInt("id");

                    String sqlRel = "DELETE FROM Persona_Direccion WHERE personaId = ? AND direccionId = ?";
                    PreparedStatement psRel = conn.prepareStatement(sqlRel);
                    psRel.setInt(1, personaId);
                    psRel.setInt(2, direccionId);
                    int rowsRel = psRel.executeUpdate();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Resultado");
                    alert.setHeaderText(null);

                    if (rowsRel > 0) {
                        String sqlCheck = "SELECT COUNT(*) AS cnt FROM Persona_Direccion WHERE direccionId = ?";
                        PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
                        psCheck.setInt(1, direccionId);
                        ResultSet rsCheck = psCheck.executeQuery();

                        if (rsCheck.next() && rsCheck.getInt("cnt") == 0) {
                            String sqlDelDir = "DELETE FROM Direcciones WHERE id = ?";
                            PreparedStatement psDelDir = conn.prepareStatement(sqlDelDir);
                            psDelDir.setInt(1, direccionId);
                            psDelDir.executeUpdate();
                        }

                        conn.commit();
                        alert.setContentText("Dirección eliminada de la persona.");
                    } else {
                        conn.rollback();
                        alert.setContentText("La persona no tenía asociada esa dirección.");
                    }
                    alert.showAndWait();

                } catch (SQLException ex) {
                    conn.rollback();
                    throw ex;
                } finally {
                    conn.setAutoCommit(true);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error al eliminar: " + ex.getMessage());
            alert.showAndWait();
        }

        fieldNombreD.clear();
        fieldTelefonoD.clear();
        fieldDireccionD.clear();
    }

    @FXML
    private void cambiarVentana(ActionEvent event, String fxmlFile) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
