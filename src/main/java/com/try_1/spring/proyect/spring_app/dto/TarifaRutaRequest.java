package com.try_1.spring.proyect.spring_app.dto;

import java.math.BigDecimal;

public class TarifaRutaRequest {

    private BigDecimal origenLon;
    private BigDecimal origenLat;
    private BigDecimal destinoLon;
    private BigDecimal destinoLat;
    private Boolean conConductor;
    private Integer dias;

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

