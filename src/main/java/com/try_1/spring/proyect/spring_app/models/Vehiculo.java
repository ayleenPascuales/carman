package com.try_1.spring.proyect.spring_app.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="Vehiculo")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="idVehiculo")
    private Integer idVehiculo;

    @ManyToOne
    @JoinColumn(name="idPropietario")
    private Propietario propietario;

    @Column(name="placa",length=20, unique=true)
    private String placa;

    @Column(name="marca",length=50)
    private String marca;

    @Column(name="modelo",length=50)
    private String modelo;

    @Column(name="anio")
    private Integer anio;
    
    @Column(name="capacidad")
    private Integer capacidad;

    @Column(name="tipoVehiculo",length=50)
    private String tipoVehiculo;

    @Enumerated(EnumType.STRING)
    @Column(name="estado")
    private EstadoVehiculo estado;

    @OneToMany(mappedBy = "vehiculo")
    private List<Telemetria> telemetrias;

    public Vehiculo() {
    }

    public Vehiculo(Propietario propietario, String placa, String marca, String modelo,
            Integer anio, Integer capacidad, String tipoVehiculo, EstadoVehiculo estado, List<Telemetria> telemetrias) {
        this.propietario = propietario;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.capacidad = capacidad;
        this.tipoVehiculo = tipoVehiculo;
        this.estado = estado;
        this.telemetrias = telemetrias;
    }

    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }

    public List<Telemetria> getTelemetrias() {
        return telemetrias;
    }

    public void setTelemetrias(List<Telemetria> telemetrias) {
        this.telemetrias = telemetrias;
    }

    
}
