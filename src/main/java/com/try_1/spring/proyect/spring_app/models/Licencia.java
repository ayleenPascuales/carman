package com.try_1.spring.proyect.spring_app.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Licencia")
public class Licencia {

    @Id 
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="idLicencia")
    private Integer idLicencia;

    @Column(name="numero", length=50, unique=true)
    private String numero;

    @Column(name="fechaExpiracion")
    private LocalDate fechaExpiracion;

    @Enumerated(EnumType.STRING)
    @Column(name="estado", length=50)
    private EstadoLicencia estado;

    @Column(name="tipoLicencia", length=50)
    private String tipoLicencia;

    @Column(name="foto", length=500)
    private String foto;

    public Licencia() {
    }

    public Licencia(EstadoLicencia estado, LocalDate fechaExpiracion, String foto, String numero, String tipoLicencia) {
        this.estado = estado;
        this.fechaExpiracion = fechaExpiracion;
        this.foto = foto;
        this.numero = numero;
        this.tipoLicencia = tipoLicencia;
    }

    public Integer getIdLicencia() {
        return idLicencia;
    }

    public void setIdLicencia(Integer idLicencia) {
        this.idLicencia = idLicencia;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDate fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public EstadoLicencia getEstado() {
        return estado;
    }

    public void setEstado(EstadoLicencia estado) {
        this.estado = estado;
    }

    public String getTipoLicencia() {
        return tipoLicencia;
    }

    public void setTipoLicencia(String tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    
  
}
