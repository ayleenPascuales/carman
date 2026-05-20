package com.try_1.spring.proyect.spring_app.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Ruta")
public class Ruta {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="idRuta")
    private Integer idRuta;

    @Column(name="origen",length=100)
    private String origen;

    @Column(name="destino",length=100)
    private String destino;

    @Column(name="distancia", precision=10, scale=2)
    private BigDecimal distancia;

    @Column(name="duracion")
    private Integer duracion;

    @Column(name="fecha")
    private LocalDate fecha;

    public Ruta() {
    }

    public Ruta(String origen, String destino, BigDecimal distancia, Integer duracion,
            LocalDate fecha) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.duracion = duracion;
        this.fecha = fecha;
    }

    public Integer getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(Integer idRuta) {
        this.idRuta = idRuta;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public BigDecimal getDistancia() {
        return distancia;
    }

    public void setDistancia(BigDecimal distancia) {
        this.distancia = distancia;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    
}
