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

    @Column(name="contraseña", length=100)
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

    public Persona() {
    }

    public Persona(String apellido, String contrasena, Integer edad, String email, EstadoPersona estado, LocalDate fechaRegistro, String nombre, String telefono, String usuario) {
        this.apellido = apellido;
        this.contrasena = contrasena;
        this.edad = edad;
        this.email = email;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
        this.nombre = nombre;
        this.telefono = telefono;
        this.usuario = usuario;
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

    
    
}
