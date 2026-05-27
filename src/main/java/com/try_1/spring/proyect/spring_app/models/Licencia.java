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

    @Column(name="fotoLicencia", length=500)
    private String fotoLicencia;

    public Licencia() {
    }

    public Licencia(Integer idLicencia, String numero, LocalDate fechaExpiracion, EstadoLicencia estado,
            String tipoLicencia, String fotoLicencia) {
        this.idLicencia = idLicencia;
        this.numero = numero;
        this.fechaExpiracion = fechaExpiracion;
        this.estado = estado;
        this.tipoLicencia = tipoLicencia;
        this.fotoLicencia = fotoLicencia;
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

    public String getFotoLicencia() {
        return fotoLicencia;
    }

    public void setFotoLicencia(String fotoLicencia) {
        this.fotoLicencia = fotoLicencia;
    }

    
}
