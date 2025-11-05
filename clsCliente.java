/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.sql.Date;

/**
 *
 * @author mabel
 */
public class clsCliente {
    String nombre, apellido, identidad, sexo, correo, direccion, telefono, filtro;
    Date year;
    int idcliente, edad;
    
    public clsCliente(){}
    
    public clsCliente(int idcliente, String nombre, String apellido, String identidad,
    int edad, String sexo, String correo, String direccion, String telefono, String filtro)
    {
        this.idcliente = idcliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.identidad = identidad;
        this.edad = edad;
        this.sexo = sexo;
        this.correo = correo;        
        this.direccion = direccion;
        this.telefono = telefono;
        this.filtro = filtro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getIdentidad() {
        return identidad;
    }

    public void setIdentidad(String identidad) {
        this.identidad = identidad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }
    
}
