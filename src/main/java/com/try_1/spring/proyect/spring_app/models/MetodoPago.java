package com.try_1.spring.proyect.spring_app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="MetodoPago")
public class MetodoPago {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="idMetodo")
    private Integer idMetodo;

    @Column(name="tipo",length=50)
    private String tipo;

    @Column(name="descripcion",length=100)
    private String descripcion;

    @Column(name="activo")
    private Boolean activo;

    public MetodoPago() {
    }

    public MetodoPago(String tipo, String descripcion, Boolean activo) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Integer getIdMetodo() {
        return idMetodo;
    }

    public void setIdMetodo(Integer idMetodo) {
        this.idMetodo = idMetodo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    
}
