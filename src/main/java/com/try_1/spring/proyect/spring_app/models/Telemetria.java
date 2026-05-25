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
@Table(name="Telemetria")
public class Telemetria {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="idTelemetria")
    private Integer idTelemetria;

    @ManyToOne
    @JoinColumn(name = "idVehiculo", referencedColumnName = "idVehiculo")
    private Vehiculo vehiculo;

    @Column(name="latitud", precision=10, scale=6)
    private BigDecimal latitud;

    @Column(name="longitud", precision=10, scale=6)
    private BigDecimal longitud;

    @Column(name="velocidad", precision=10, scale=2)
    private BigDecimal velocidad;

    @Column(name="timestampRegistro")
    private LocalDateTime timestampRegistro;

    public Telemetria() {
    }

    public Telemetria(Vehiculo vehiculo, BigDecimal latitud, BigDecimal longitud,
            BigDecimal velocidad, LocalDateTime timestampRegistro) {
        this.vehiculo = vehiculo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.velocidad = velocidad;
        this.timestampRegistro = timestampRegistro;
    }

    public Integer getIdTelemetria() {
        return idTelemetria;
    }

    public void setIdTelemetria(Integer idTelemetria) {
        this.idTelemetria = idTelemetria;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public BigDecimal getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(BigDecimal velocidad) {
        this.velocidad = velocidad;
    }

    public LocalDateTime getTimestampRegistro() {
        return timestampRegistro;
    }

    public void setTimestampRegistro(LocalDateTime timestampRegistro) {
        this.timestampRegistro = timestampRegistro;
    }

    


}
