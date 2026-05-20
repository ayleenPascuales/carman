package com.try_1.spring.proyect.spring_app.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Conductor")
public class Conductor {

    @Id
    @Column(name="idConductor")
    private Integer idConductor;

    @OneToOne
    @JoinColumn(name="idPersona")
    private Persona persona;

    @OneToOne
    @JoinColumn(name="idLicencia")
    private Licencia licenciaConductor;

    @Column(name="disponibilidad")
    private Boolean disponibilidad;

    @Column(name="calificacion", precision=3, scale=2)
    private BigDecimal calificacion;

    public Conductor() {
    }

    public Conductor(Integer idConductor, Persona persona, Licencia licenciaConductor, Boolean disponibilidad,
            BigDecimal calificacion) {
        this.idConductor = idConductor;
        this.persona = persona;
        this.licenciaConductor = licenciaConductor;
        this.disponibilidad = disponibilidad;
        this.calificacion = calificacion;
    }

    public Integer getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(Integer idConductor) {
        this.idConductor = idConductor;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Licencia getLicenciaConductor() {
        return licenciaConductor;
    }

    public void setLicenciaConductor(Licencia licenciaConductor) {
        this.licenciaConductor = licenciaConductor;
    }

    public Boolean getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public BigDecimal getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(BigDecimal calificacion) {
        this.calificacion = calificacion;
    }

    
}
