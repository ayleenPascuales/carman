package com.try_1.spring.proyect.spring_app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class CrearReservaRequest {

    private Integer idCliente;
    private Integer idVehiculo;
    private Integer idConductor;
    private String origen;
    private String destino;
    private BigDecimal origenLon;
    private BigDecimal origenLat;
    private BigDecimal destinoLon;
    private BigDecimal destinoLat;
    private LocalDate fechaServicio;
    private LocalTime horaEntrega;
    private BigDecimal precioEstimado;
    private Boolean conConductor;
    private Integer dias;

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Integer getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(Integer idConductor) {
        this.idConductor = idConductor;
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

    public LocalDate getFechaServicio() {
        return fechaServicio;
    }

    public void setFechaServicio(LocalDate fechaServicio) {
        this.fechaServicio = fechaServicio;
    }

    public LocalTime getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(LocalTime horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    public BigDecimal getPrecioEstimado() {
        return precioEstimado;
    }

    public void setPrecioEstimado(BigDecimal precioEstimado) {
        this.precioEstimado = precioEstimado;
    }

    public Boolean getConConductor() {
        return conConductor;
    }

    public void setConConductor(Boolean conConductor) {
        this.conConductor = conConductor;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }
}
