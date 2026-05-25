package com.try_1.spring.proyect.spring_app.models;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @JoinColumn(name="idCliente", referencedColumnName = "idCliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name="idVehiculo", referencedColumnName = "idVehiculo")
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name="idPropietario", referencedColumnName = "idPropietario")
    private Propietario propietario;

    @ManyToOne
    @JoinColumn(name="idConductor", referencedColumnName = "idConductor")
    private Conductor conductor;

    @Column(name="fechaInicio")
    private LocalDate fechaInicio;

    @Column(name="fechaFin")
    private LocalDate fechaFin;

    @Column(name="condiciones", length=255)
    private String condiciones;

    @Enumerated(EnumType.STRING)
    @Column(name="estado", length=50)
    private EstadoContrato estado;

    @Column(name="archivoPdf", length=500)
    private String archivoPdf;

    @Column(name="firmaCliente", length=500)
    private String firmaCliente;

    @Column(name="fechaGeneracion")
    private LocalDate fechaGeneracion;

    public ContratoAlquiler() {
    }

    public ContratoAlquiler(String archivoPdf, Cliente cliente, String condiciones, Conductor conductor, EstadoContrato estado, LocalDate fechaFin, LocalDate fechaGeneracion, LocalDate fechaInicio, String firmaCliente, Integer idContratoAlquiler, Propietario propietario, Reserva reserva, Vehiculo vehiculo) {
        this.archivoPdf = archivoPdf;
        this.cliente = cliente;
        this.condiciones = condiciones;
        this.conductor = conductor;
        this.estado = estado;
        this.fechaFin = fechaFin;
        this.fechaGeneracion = fechaGeneracion;
        this.fechaInicio = fechaInicio;
        this.firmaCliente = firmaCliente;
        this.idContratoAlquiler = idContratoAlquiler;
        this.propietario = propietario;
        this.reserva = reserva;
        this.vehiculo = vehiculo;
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

    public EstadoContrato getEstado() {
        return estado;
    }

    public void setEstado(EstadoContrato estado) {
        this.estado = estado;
    }

    public String getArchivoPdf() {
        return archivoPdf;
    }

    public void setArchivoPdf(String archivoPdf) {
        this.archivoPdf = archivoPdf;
    }

    public String getFirmaCliente() {
        return firmaCliente;
    }

    public void setFirmaCliente(String firmaCliente) {
        this.firmaCliente = firmaCliente;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    
   
}
