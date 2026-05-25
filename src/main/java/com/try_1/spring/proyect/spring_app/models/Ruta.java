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

    @Column(name="origenLon", precision=10, scale=6)
    private BigDecimal origenLon;

    @Column(name="origenLat", precision=10, scale=6)
    private BigDecimal origenLat;

    @Column(name="destino",length=100)
    private String destino;

    @Column(name="destinoLon", precision=10, scale=6)
    private BigDecimal destinoLon;

    @Column(name="destinoLat", precision=10, scale=6)
    private BigDecimal destinoLat;

    @Column(name="distancia", precision=10, scale=2)
    private BigDecimal distancia;

    @Column(name="duracion")
    private Integer duracion;

    @Column(name="fecha")
    private LocalDate fecha;

    public Ruta() {
    }

    public Ruta(BigDecimal distancia, Integer duracion){
        this.distancia = distancia;
        this.duracion = duracion;
    }

    public Ruta(String destino, BigDecimal destinoLat, BigDecimal destinoLon, BigDecimal distancia, Integer duracion, LocalDate fecha, Integer idRuta, String origen, BigDecimal origenLat, BigDecimal origenLon) {
        this.destino = destino;
        this.destinoLat = destinoLat;
        this.destinoLon = destinoLon;
        this.distancia = distancia;
        this.duracion = duracion;
        this.fecha = fecha;
        this.idRuta = idRuta;
        this.origen = origen;
        this.origenLat = origenLat;
        this.origenLon = origenLon;
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

    public BigDecimal getOrigenLon() {
        return origenLon;
    }

    public void setOrigenLon(BigDecimal origenLon) {
        this.origenLon = origenLon;
    }

    public BigDecimal getOrigenLat() {
        return origenLat;
    }

    public void setOrigenLat(BigDecimal origenLat) {
        this.origenLat = origenLat;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public BigDecimal getDestinoLon() {
        return destinoLon;
    }

    public void setDestinoLon(BigDecimal destinoLon) {
        this.destinoLon = destinoLon;
    }

    public BigDecimal getDestinoLat() {
        return destinoLat;
    }

    public void setDestinoLat(BigDecimal destinoLat) {
        this.destinoLat = destinoLat;
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
