/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package agenda;

/**
 *
 * @author alexm
 */
public interface PersonaServiceInter {
    void agregarPersonaCompleta(String nombre, String direccion, String telefono) throws Exception;
    void agregarTelefonoExistente(String nombre, String telefono) throws Exception;
    void agregarDireccionExistente(String nombre, String direccion) throws Exception;
    void actualizarNombre(int idPersona, String nuevoNombre) throws Exception;
    void actualizarTelefono(String nombrePersona, int idTelefono, String nuevoTelefono) throws Exception;
    void eliminarPersona(String nombre) throws Exception;
    void eliminarTelefono(String nombre, String telefono) throws Exception;
    void eliminarDireccion(String nombre, String direccion) throws Exception;
}
