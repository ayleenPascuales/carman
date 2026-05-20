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

    @Column(name="numero", length=50)
    private String numero;

    @Column(name="fechaExpiracion")
    private LocalDate fechaExpiracion;

    @Enumerated(EnumType.STRING)
    @Column(name="estado", length=50)
    private EstadoLicencia estado;

    @Column(name="tipoLicencia", length=50)
    private String tipoLicencia;

    public Licencia() {
    }

    public Licencia(String numero, LocalDate fechaExpiracion, EstadoLicencia estado, String tipoLicencia) {
        this.numero = numero;
        this.fechaExpiracion = fechaExpiracion;
        this.estado = estado;
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

  
}
