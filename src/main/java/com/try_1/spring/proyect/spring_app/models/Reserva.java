package com.try_1.spring.proyect.spring_app.models;

import java.math.BigDecimal;
import java.time.LocalDate;
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
@Table(name="Reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="idReserva")
    private Integer idReserva;

    @ManyToOne
    @JoinColumn(name="idCliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name="idVehiculo")
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name="idConductor")
    private Conductor conductor;

    @ManyToOne
    @JoinColumn(name="idRuta")
    private Ruta ruta;

    @Column(name="horaEntrega")
    private LocalDateTime horaEntrega;

    @Column(name="fechaReserva")
    private LocalDate fechaReserva;

    @Column(name="fechaServicio")
    private LocalDate fechaServicio;

    @Enumerated(EnumType.STRING)
    @Column(name="estado")
    private EstadoReserva estado;

    @Column(name="precioEstimado", precision=10, scale=2)
    private BigDecimal precioEstimado;

    public Reserva() {
    }

    public Reserva(Cliente cliente, Conductor conductor, EstadoReserva estado, LocalDate fechaReserva, LocalDate fechaServicio, LocalDateTime horaEntrega, BigDecimal precioEstimado, Ruta ruta, Vehiculo vehiculo) {
        this.cliente = cliente;
        this.conductor = conductor;
        this.estado = estado;
        this.fechaReserva = fechaReserva;
        this.fechaServicio = fechaServicio;
        this.horaEntrega = horaEntrega;
        this.precioEstimado = precioEstimado;
        this.ruta = ruta;
        this.vehiculo = vehiculo;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Conductor getConductor() {
        return conductor;
    }

    public void setConductor(Conductor conductor) {
        this.conductor = conductor;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public LocalDateTime getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(LocalDateTime horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public LocalDate getFechaServicio() {
        return fechaServicio;
    }

    public void setFechaServicio(LocalDate fechaServicio) {
        this.fechaServicio = fechaServicio;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public BigDecimal getPrecioEstimado() {
        return precioEstimado;
    }

    public void setPrecioEstimado(BigDecimal precioEstimado) {
        this.precioEstimado = precioEstimado;
    }

    


    
    
}
