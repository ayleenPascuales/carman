package com.try_1.spring.proyect.spring_app.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Pago")
public class Pago {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="idPago")
    private Integer idPago;

    @ManyToOne
    @JoinColumn(name="idReserva", referencedColumnName = "idReserva")
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name="idOrdenPago", referencedColumnName = "idOrdenPago")
    private OrdenPago ordenPago;

    @ManyToOne
    @JoinColumn(name="idMetodo", referencedColumnName = "idMetodo")
    private MetodoPago metodo;

    @Column(name="monto", precision=10, scale=2)
    private BigDecimal monto;

    @Column(name="fechaHoraPago")
    private LocalDateTime fechaHoraPago;

    @Enumerated(EnumType.STRING)
    @Column(name="estado", length=50)
    private EstadoPago estado;

    public Pago() {
    }

    public Pago(Reserva reserva, OrdenPago ordenPago, MetodoPago metodo, BigDecimal monto, LocalDateTime fechaHoraPago,
            EstadoPago estado) {
        this.reserva = reserva;
        this.ordenPago = ordenPago;
        this.metodo = metodo;
        this.monto = monto;
        this.fechaHoraPago = fechaHoraPago;
        this.estado = estado;
    }

    public Integer getIdPago() {
        return idPago;
    }

    public void setIdPago(Integer idPago) {
        this.idPago = idPago;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public OrdenPago getOrdenPago() {
        return ordenPago;
    }

    public void setOrdenPago(OrdenPago ordenPago) {
        this.ordenPago = ordenPago;
    }

    public MetodoPago getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoPago metodo) {
        this.metodo = metodo;
    }

    public void setMetodos(MetodoPago metodo) {
        this.metodo = metodo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDateTime getFechaHoraPago() {
        return fechaHoraPago;
    }

    public void setFechaHoraPago(LocalDateTime fechaHoraPago) {
        this.fechaHoraPago = fechaHoraPago;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }

    



}
