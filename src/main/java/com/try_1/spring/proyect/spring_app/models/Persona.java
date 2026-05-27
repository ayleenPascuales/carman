package com.try_1.spring.proyect.spring_app.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Persona")
public class Persona {

    @Id
    @Column(name = "idPersona")
    private Integer idPersona;

    @Column(name = "nombre", length= 100)
    private String nombre;

    @Column(name = "apellido", length = 100)
    private String apellido;

    @Column(name = "email", length= 100)
    private String email;

    @Column(name="contrasena", length=100)
    private String contrasena;

    @Column(name="telefono", length=20)
    private String telefono;

    @Column(name="fechaRegistro")
    private LocalDate fechaRegistro;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 50)
    private EstadoPersona estado;

    @Column(name="usuario", length=100)
    private String usuario;

    @Column(name="edad")
    private Integer edad;

    @Column(name="ciudad", length=50)
    private String ciudad;

    public Persona() {
    }

    public Persona(Integer idPersona, String nombre, String apellido, String email, String contrasena, String telefono,
            LocalDate fechaRegistro, EstadoPersona estado, String usuario, Integer edad, String ciudad) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
        this.estado = estado;
        this.usuario = usuario;
        this.edad = edad;
        this.ciudad = ciudad;
    }

    public Integer getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public EstadoPersona getEstado() {
        return estado;
    }

    public void setEstado(EstadoPersona estado) {
        this.estado = estado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

       
}
