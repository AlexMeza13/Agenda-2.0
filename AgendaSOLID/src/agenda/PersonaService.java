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

public class PersonaService implements PersonaServiceInter {

    private final PersonaRepoInter personaRepo;
    private final DireccionRepoInter direccionRepo;
    private final TelefonoRepoInter telefonoRepo;
    private final PersonaDirecRepoInter pdRepo;

    public PersonaService(PersonaRepoInter personaRepo, DireccionRepoInter direccionRepo, TelefonoRepoInter telefonoRepo, PersonaDirecRepoInter pdRepo) {
        this.personaRepo = personaRepo;
        this.direccionRepo = direccionRepo;
        this.telefonoRepo = telefonoRepo;
        this.pdRepo = pdRepo;
    }

    @Override
    public void agregarPersonaCompleta(String nombre, String direccion, String telefono) throws Exception {
        try (Connection conn = Conexion.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int personaId = personaRepo.agregar(new Persona(nombre), conn);
                int direccionId = direccionRepo.agregar(new Direccion(direccion), conn);
                pdRepo.agregar(personaId, direccionId, conn);
                telefonoRepo.agregar(new Telefono(personaId, telefono), conn);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    @Override
    public void agregarTelefonoExistente(String nombre, String telefono) throws Exception {
        try (Connection conn = Conexion.getConnection()) {
            int personaId = personaRepo.obtenerIdPorNombre(nombre, conn);
            if (personaId == -1) {
                throw new Exception("Persona no encontrada");
            }
            telefonoRepo.agregar(new Telefono(personaId, telefono), conn);
        }
    }

    @Override
    public void agregarDireccionExistente(String nombre, String direccion) throws Exception {
        try (Connection conn = Conexion.getConnection()) {
            int personaId = personaRepo.obtenerIdPorNombre(nombre, conn);
            if (personaId == -1) {
                throw new Exception("Persona no encontrada");
            }

            int direccionId = direccionRepo.agregar(new Direccion(direccion), conn);

            if (!pdRepo.existe(personaId, direccionId, conn)) {
                pdRepo.agregar(personaId, direccionId, conn);
            } 
        }
    }

    @Override
    public void actualizarNombre(int idPersona, String nuevoNombre) throws Exception {
        try (Connection conn = Conexion.getConnection()) {
            personaRepo.actualizarNombre(idPersona, nuevoNombre, conn);
        }
    }

    @Override
    public void actualizarTelefono(String nombrePersona, int idTelefono, String nuevoTelefono) throws Exception {
        try (Connection conn = Conexion.getConnection()) {
            int personaId = personaRepo.obtenerIdPorNombre(nombrePersona, conn);
            if (personaId == -1) {
                throw new Exception("Persona no encontrada");
            }
            telefonoRepo.actualizar(idTelefono, nuevoTelefono, personaId, conn);
        }
    }

    @Override
    public void eliminarPersona(String nombre) throws Exception {
        try (Connection conn = Conexion.getConnection()) {
            personaRepo.eliminar(nombre, conn);
        }
    }

    @Override
    public void eliminarTelefono(String nombre, String telefono) throws Exception {
        try (Connection conn = Conexion.getConnection()) {
            int personaId = personaRepo.obtenerIdPorNombre(nombre, conn);
            if (personaId == -1) {
                throw new Exception("Persona no encontrada");
            }
            telefonoRepo.eliminar(personaId, telefono, conn);
        }
    }

    @Override
    public void eliminarDireccion(String nombre, String direccion) throws Exception {
        try (Connection conn = Conexion.getConnection()) {
            int personaId = personaRepo.obtenerIdPorNombre(nombre, conn);
            if (personaId == -1) {
                throw new Exception("Persona no encontrada");
            }

            int direccionId = direccionRepo.obtenerIdPorNombre(direccion, conn);
            if (direccionId == -1) {
                throw new Exception("Dirección no encontrada");
            }

            pdRepo.eliminar(personaId, direccionId, conn);
            direccionRepo.eliminarSiNoUsada(direccionId, conn);
        }
    }
}
