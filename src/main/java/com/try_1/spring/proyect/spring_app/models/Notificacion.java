package com.try_1.spring.proyect.spring_app.models;

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

    @Enumerated(EnumType.STRING)
    @Column(name="tipo", length=50)
    private TipoNotificacion tipo;

    @Column(name="mensaje", length=255)
    private String mensaje;

    @Column(name="fechaHoraEnvio")
    private LocalDateTime fechaHoraEnvio;

    @Column(name="leida")
    private Boolean leida;

    public Notificacion() {
    }

    public Notificacion(LocalDateTime fechaHoraEnvio, Integer idNotificacion, Boolean leida, String mensaje, Pago pago, Reserva reserva, TipoNotificacion tipo) {
        this.fechaHoraEnvio = fechaHoraEnvio;
        this.idNotificacion = idNotificacion;
        this.leida = leida;
        this.mensaje = mensaje;
        this.pago = pago;
        this.reserva = reserva;
        this.tipo = tipo;
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

    public TipoNotificacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacion tipo) {
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

    public Boolean getLeida() {
        return leida;
    }

    public void setLeida(Boolean leida) {
        this.leida = leida;
    }

    
}
