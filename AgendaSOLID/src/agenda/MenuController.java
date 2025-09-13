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
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author alexm
 */
public class MenuController implements Initializable {

    private final PersonaServiceInter personaService = new PersonaService(
            new PersonaBD(),
            new DireccionBD(),
            new TelefonoBD(),
            new PersonaDirecBD()
    );

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

            var ps = conn.prepareStatement(query);
            var rs = ps.executeQuery();

            javafx.scene.control.TableView<javafx.collections.ObservableList<String>> tableView = new javafx.scene.control.TableView<>();

            java.sql.ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();

            for (int i = 1; i <= colCount; i++) {
                final int j = i;
                javafx.scene.control.TableColumn<javafx.collections.ObservableList<String>, String> col
                        = new javafx.scene.control.TableColumn<>(rsmd.getColumnName(i));
                col.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().get(j - 1)));
                tableView.getColumns().add(col);
            }

            javafx.collections.ObservableList<javafx.collections.ObservableList<String>> data
                    = javafx.collections.FXCollections.observableArrayList();

            while (rs.next()) {
                javafx.collections.ObservableList<String> row = javafx.collections.FXCollections.observableArrayList();
                for (int i = 1; i <= colCount; i++) {
                    row.add(rs.getString(i) != null ? rs.getString(i) : "");
                }
                data.add(row);
            }

            tableView.setItems(data);

            Stage stage = new Stage();
            stage.setTitle("Tabla Persona con Direcciones");
            stage.setScene(new Scene(new javafx.scene.layout.VBox(tableView), 600, 400));
            stage.show();

        } catch (Exception e) {
            showAlertError("Error mostrando tabla: " + e.getMessage());
        }
    }

    @FXML
    private void evenButtonRT(ActionEvent event) {
        try (Connection conn = Conexion.getConnection()) {
            String query = "SELECT * FROM Telefonos";
            var ps = conn.prepareStatement(query);
            var rs = ps.executeQuery();

            javafx.scene.control.TableView<javafx.collections.ObservableList<String>> tableView = new javafx.scene.control.TableView<>();
            java.sql.ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();

            for (int i = 1; i <= colCount; i++) {
                final int j = i;
                javafx.scene.control.TableColumn<javafx.collections.ObservableList<String>, String> col
                        = new javafx.scene.control.TableColumn<>(rsmd.getColumnName(i));
                col.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().get(j - 1)));
                tableView.getColumns().add(col);
            }

            javafx.collections.ObservableList<javafx.collections.ObservableList<String>> data
                    = javafx.collections.FXCollections.observableArrayList();

            while (rs.next()) {
                javafx.collections.ObservableList<String> row = javafx.collections.FXCollections.observableArrayList();
                for (int i = 1; i <= colCount; i++) {
                    row.add(rs.getString(i) != null ? rs.getString(i) : "");
                }
                data.add(row);
            }

            tableView.setItems(data);

            Stage stage = new Stage();
            stage.setTitle("Tabla Telefonos");
            stage.setScene(new Scene(new javafx.scene.layout.VBox(tableView), 400, 300));
            stage.show();

        } catch (Exception e) {
            showAlertError("Error mostrando tabla: " + e.getMessage());
        }
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

    private void cambiarVentana(ActionEvent event, String fxmlFile) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void evenButtonAdd(ActionEvent event) {
        String nombre = fieldNombreA.getText().trim();
        String direccion = fieldDireccionA.getText().trim();
        String telefono = fieldTelefonoA.getText().trim();

        try {
            if (!nombre.isEmpty() && !direccion.isEmpty() && !telefono.isEmpty()) {
                personaService.agregarPersonaCompleta(nombre, direccion, telefono);
                showAlert("Persona creada con éxito");
            } else if (!nombre.isEmpty() && !telefono.isEmpty()) {
                personaService.agregarTelefonoExistente(nombre, telefono);
                showAlert("Teléfono agregado con éxito");
            } else if (!nombre.isEmpty() && !direccion.isEmpty()) {
                personaService.agregarDireccionExistente(nombre, direccion);
                showAlert("Dirección agregada con éxito");
            } else {
                showAlertWarning("Combinación inválida");
            }
        } catch (Exception e) {
            showAlertError(e.getMessage());
        }

        fieldNombreA.clear();
        fieldDireccionA.clear();
        fieldTelefonoA.clear();
    }

    @FXML
    private void evenButtonUp(ActionEvent event) {
        try {
            if (!fieldIdPersona.getText().isEmpty() && !fieldNombreUp.getText().isEmpty()) {
                personaService.actualizarNombre(
                        Integer.parseInt(fieldIdPersona.getText()), fieldNombreUp.getText()
                );
                showAlert("Nombre actualizado");
            }
            if (!fieldIdTelefono.getText().isEmpty() && !fieldTelefonoUp.getText().isEmpty()
                    && !fieldNombreUp.getText().isEmpty()) {
                personaService.actualizarTelefono(fieldNombreUp.getText(),
                        Integer.parseInt(fieldIdTelefono.getText()), fieldTelefonoUp.getText()
                );
                showAlert("Teléfono actualizado");
            }
        } catch (Exception e) {
            showAlertError(e.getMessage());
        }

        fieldIdPersona.clear();
        fieldNombreUp.clear();
        fieldIdTelefono.clear();
        fieldTelefonoUp.clear();
    }

    @FXML
    private void evenButtonDel(ActionEvent event) {
        String nombre = fieldNombreD.getText().trim();
        String telefono = fieldTelefonoD.getText().trim();
        String direccion = fieldDireccionD.getText().trim();

        try {
            if (!nombre.isEmpty() && telefono.isEmpty() && direccion.isEmpty()) {
                personaService.eliminarPersona(nombre);
                showAlert("Persona eliminada");
            } else if (!nombre.isEmpty() && !telefono.isEmpty()) {
                personaService.eliminarTelefono(nombre, telefono);
                showAlert("Teléfono eliminado");
            } else if (!nombre.isEmpty() && !direccion.isEmpty()) {
                personaService.eliminarDireccion(nombre, direccion);
                showAlert("Dirección eliminada");
            } else {
                showAlertWarning("Debe indicar nombre y teléfono o dirección");
            }
        } catch (Exception e) {
            showAlertError(e.getMessage());
        }

        fieldNombreD.clear();
        fieldTelefonoD.clear();
        fieldDireccionD.clear();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showAlertWarning(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showAlertError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
