package com.try_1.spring.proyect.spring_app.dto;

public class RegistrarPagoRequest {

    private Integer idOrdenPago;
    private Integer idMetodo;

    public Integer getIdOrdenPago() {
        return idOrdenPago;
    }

    public void setIdOrdenPago(Integer idOrdenPago) {
        this.idOrdenPago = idOrdenPago;
    }

    public Integer getIdMetodo() {
        return idMetodo;
    }

    public void setIdMetodo(Integer idMetodo) {
        this.idMetodo = idMetodo;
    }
}
