package com.try_1.spring.proyect.spring_app.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="idNotificacion")
    private Integer idNotificacion;

    @ManyToOne
    @JoinColumn(name="idReserva")
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name="idPago")
    private Pago pago;

    @Column(name="tipo", length=50)
    private String tipo;

    @Column(name="mensaje", length=255)
    private String mensaje;

    @Column(name="fechaHoraEnvio")
    private LocalDateTime fechaHoraEnvio;

    public Notificacion() {
    }

    public Notificacion(Reserva reserva, Pago pago, String tipo, String mensaje, LocalDateTime fechaHoraEnvio) {
        this.reserva = reserva;
        this.pago = pago;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fechaHoraEnvio = fechaHoraEnvio;
    }

    public Integer getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(Integer idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaHoraEnvio() {
        return fechaHoraEnvio;
    }

    public void setFechaHoraEnvio(LocalDateTime fechaHoraEnvio) {
        this.fechaHoraEnvio = fechaHoraEnvio;
    }

    

}
