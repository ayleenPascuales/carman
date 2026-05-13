package com.try_1.spring.proyect.spring_app.models;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="ContratoAlquiler")
public class ContratoAlquiler {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="idContratoAlquiler")
    private Integer idContratoAlquiler;

    @OneToOne
    @JoinColumn(name="idReserva", unique = true)
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name="idCliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name="idVehiculo")
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name="idPropietario")
    private Propietario propietario;

    @ManyToOne
    @JoinColumn(name="idConductor")
    private Conductor conductor;

    @Column(name="fechaInicio")
    private LocalDate fechaInicio;

    @Column(name="fechaFin")
    private LocalDate fechaFin;

    @Column(name="condiciones", length=255)
    private String condiciones;

    public ContratoAlquiler() {
    }

    public ContratoAlquiler(Reserva reserva, Cliente cliente, Vehiculo vehiculo, Propietario propietario,
            Conductor conductor, LocalDate fechaInicio, LocalDate fechaFin, String condiciones) {
        this.reserva = reserva;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.propietario = propietario;
        this.conductor = conductor;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.condiciones = condiciones;
    }

    public Integer getIdContratoAlquiler() {
        return idContratoAlquiler;
    }

    public void setIdContratoAlquiler(Integer idContratoAlquiler) {
        this.idContratoAlquiler = idContratoAlquiler;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
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

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public Conductor getConductor() {
        return conductor;
    }

    public void setConductor(Conductor conductor) {
        this.conductor = conductor;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones = condiciones;
    }

    




}
