package com.try_1.spring.proyect.spring_app.models;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Propietario")
public class Propietario {

    @Id
    @Column(name="idPropietario")
    private Integer idPropietario;

    @ManyToOne
    @JoinColumn(name="idPersona")
    private Persona persona;

    @OneToOne
    @JoinColumn(name="idLicencia", referencedColumnName = "idLicencia")
    private Licencia licenciaPropietario;

    @OneToMany(mappedBy = "propietario")
    private List<Vehiculo> vehiculos;

    @Column(name="tarjetaPropiedad", columnDefinition = "VARCHAR(MAX)")
    private String tarjetaPropiedad;

    @Column(name="totalPrestamos")
    private Integer totalPrestamos;

    public Propietario() {
    }

    public Propietario(Integer idPropietario, Persona persona, Licencia licenciaPropietario, List<Vehiculo> vehiculos,
            String tarjetaPropiedad, Integer totalPrestamos) {
        this.idPropietario = idPropietario;
        this.persona = persona;
        this.licenciaPropietario = licenciaPropietario;
        this.vehiculos = vehiculos;
        this.tarjetaPropiedad = tarjetaPropiedad;
        this.totalPrestamos = totalPrestamos;
    }

    public Integer getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(Integer idPropietario) {
        this.idPropietario = idPropietario;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Licencia getLicenciaPropietario() {
        return licenciaPropietario;
    }

    public void setLicenciaPropietario(Licencia licenciaPropietario) {
        this.licenciaPropietario = licenciaPropietario;
    }

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    public String getTarjetaPropiedad() {
        return tarjetaPropiedad;
    }

    public void setTarjetaPropiedad(String tarjetaPropiedad) {
        this.tarjetaPropiedad = tarjetaPropiedad;
    }

    public Integer getTotalPrestamos() {
        return totalPrestamos;
    }

    public void setTotalPrestamos(Integer totalPrestamos) {
        this.totalPrestamos = totalPrestamos;
    }
}

      
