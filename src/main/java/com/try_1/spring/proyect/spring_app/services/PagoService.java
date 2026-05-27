package com.try_1.spring.proyect.spring_app.services;

import java.time.LocalDateTime;
import java.util.List;

import com.try_1.spring.proyect.spring_app.dto.RegistrarPagoRequest;
import com.try_1.spring.proyect.spring_app.models.OrdenPago;
import com.try_1.spring.proyect.spring_app.models.Pago;

public interface PagoService {
    List<Pago> listar();
    Pago guardar(Pago pago);
    Pago buscarPorId(Integer id);
    void eliminar(Integer id);
    List<Pago> buscarPorFechaHoraPago(LocalDateTime fechaHoraPago);
    public Pago crearPagoDesdeOrden(OrdenPago orden);
    public Pago procesarPago(Integer idPago);
    List<Pago> buscarPorCliente(Integer idCliente);
    List<Pago> buscarPorReserva(Integer idReserva);
    Pago registrarPagoCompleto(RegistrarPagoRequest request);
}
