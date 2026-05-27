package com.try_1.spring.proyect.spring_app.services;

import java.time.LocalDate;
import java.util.List;

import com.try_1.spring.proyect.spring_app.models.OrdenPago;

public interface OrdenPagoService {
    List<OrdenPago> listar();
    OrdenPago guardar(OrdenPago ordenPago);
    OrdenPago buscarPorId(Integer id);
    void eliminar(Integer id);
    List<OrdenPago> buscarPorFechaEmision(LocalDate fechaEmision);
    public byte[] generarPdf(Integer idOrdenPago);
    public OrdenPago generarOrdenDesdeReserva(Integer idReserva);
    List<OrdenPago> buscarPorCliente(Integer idCliente);
    List<OrdenPago> buscarPorReserva(Integer idReserva);
}
