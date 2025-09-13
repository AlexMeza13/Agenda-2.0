/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agenda;

/**
 *
 * @author alexm
 */
public class Direccion {
    private int id;
    private String direccion;

    public Direccion() {}
    public Direccion(String direccion) { this.direccion = direccion; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}

