package com.try_1.spring.proyect.spring_app.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="OrdenPago")
public class OrdenPago {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="idOrdenPago")
    private Integer idOrdenPago;

    @OneToOne
    @JoinColumn(name="idReserva", unique=true)
    private Reserva reserva;

    @Column(name="fechaEmision")
    private LocalDate fechaEmision;

    @Column(name="total", precision=10, scale=2)
    private BigDecimal total;

    @Column(name="detalles", length=255)
    private String detalles;

    @Column(name = "rutaPdf", length = 500)
    private String rutaPdf;

    @Enumerated(EnumType.STRING)
    private EstadoOrdenPago estado;

    public OrdenPago() {
    }

    public OrdenPago(String detalles, EstadoOrdenPago estado, LocalDate fechaEmision, Integer idOrdenPago, Reserva reserva, String rutaPdf, BigDecimal total) {
        this.detalles = detalles;
        this.estado = estado;
        this.fechaEmision = fechaEmision;
        this.idOrdenPago = idOrdenPago;
        this.reserva = reserva;
        this.rutaPdf = rutaPdf;
        this.total = total;
    }

    public Integer getIdOrdenPago() {
        return idOrdenPago;
    }

    public void setIdOrdenPago(Integer idOrdenPago) {
        this.idOrdenPago = idOrdenPago;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public String getRutaPdf() {
        return rutaPdf;
    }

    public void setRutaPdf(String rutaPdf) {
        this.rutaPdf = rutaPdf;
    }

    public EstadoOrdenPago getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrdenPago estado) {
        this.estado = estado;
    }

    

    

}
