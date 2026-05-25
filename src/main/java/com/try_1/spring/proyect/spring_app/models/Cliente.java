package com.try_1.spring.proyect.spring_app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Cliente")
public class Cliente {

    @Id
    @Column(name="idCliente")
    private Integer idCliente;

    @ManyToOne
    @JoinColumn(name="idPersona")
    private Persona persona;

    @OneToOne
    @JoinColumn(name="idLicencia", referencedColumnName = "idLicencia")
    private Licencia licenciaCliente;

    public Cliente() {
    }

    public Cliente(Integer idCliente, Persona persona, Licencia licenciaCliente) {
        this.idCliente = idCliente;
        this.persona = persona;
        this.licenciaCliente = licenciaCliente;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Licencia getLicenciaCliente() {
        return licenciaCliente;
    }

    public void setLicenciaCliente(Licencia licenciaCliente) {
        this.licenciaCliente = licenciaCliente;
    }

    

    
}
