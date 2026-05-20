package com.try_1.spring.proyect.spring_app.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="EstadoTecnicoVehiculo")
public class EstadoTecnicoVehiculo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEstado")
    private Integer idEstado;

    @ManyToOne
    @JoinColumn(name = "idVehiculo")
    private Vehiculo vehiculo;

    @Column(name = "kilometraje")
    private BigDecimal kilometraje;

    @Column(name = "estadoMotor")
    private String estadoMotor;

    @Column(name = "estadoFrenos")
    private String estadoFrenos;

    @Column(name = "estadoLlantas")
    private String estadoLlantas;

    @Column(name = "estadoBateria")
    private BigDecimal estadoBateria;

    @Column(name = "necesitaMantenimiento")
    private Boolean necesitaMantenimiento;

    @Column(name = "fechaRegistro")
    private LocalDateTime fechaRegistro;

    public EstadoTecnicoVehiculo() {
    }

    public EstadoTecnicoVehiculo(Vehiculo vehiculo, BigDecimal kilometraje, String estadoMotor, String estadoFrenos,
            String estadoLlantas, BigDecimal estadoBateria, Boolean necesitaMantenimiento,
            LocalDateTime fechaRegistro) {
        this.vehiculo = vehiculo;
        this.kilometraje = kilometraje;
        this.estadoMotor = estadoMotor;
        this.estadoFrenos = estadoFrenos;
        this.estadoLlantas = estadoLlantas;
        this.estadoBateria = estadoBateria;
        this.necesitaMantenimiento = necesitaMantenimiento;
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public BigDecimal getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(BigDecimal kilometraje) {
        this.kilometraje = kilometraje;
    }

    public String getEstadoMotor() {
        return estadoMotor;
    }

    public void setEstadoMotor(String estadoMotor) {
        this.estadoMotor = estadoMotor;
    }

    public String getEstadoFrenos() {
        return estadoFrenos;
    }

    public void setEstadoFrenos(String estadoFrenos) {
        this.estadoFrenos = estadoFrenos;
    }

    public String getEstadoLlantas() {
        return estadoLlantas;
    }

    public void setEstadoLlantas(String estadoLlantas) {
        this.estadoLlantas = estadoLlantas;
    }

    public BigDecimal getEstadoBateria() {
        return estadoBateria;
    }

    public void setEstadoBateria(BigDecimal estadoBateria) {
        this.estadoBateria = estadoBateria;
    }

    public Boolean getNecesitaMantenimiento() {
        return necesitaMantenimiento;
    }

    public void setNecesitaMantenimiento(Boolean necesitaMantenimiento) {
        this.necesitaMantenimiento = necesitaMantenimiento;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    
}
