package com.try_1.spring.proyect.spring_app.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    public OrdenPago() {
    }

    public OrdenPago(Reserva reserva, LocalDate fechaEmision, BigDecimal total, String detalles) {
        this.reserva = reserva;
        this.fechaEmision = fechaEmision;
        this.total = total;
        this.detalles = detalles;
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

    
    


}
