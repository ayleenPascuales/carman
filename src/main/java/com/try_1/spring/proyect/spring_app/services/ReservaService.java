package com.try_1.spring.proyect.spring_app.services;

import java.time.LocalDate;
import java.util.List;

import com.try_1.spring.proyect.spring_app.dto.CrearReservaRequest;
import com.try_1.spring.proyect.spring_app.dto.HistorialItemDTO;
import com.try_1.spring.proyect.spring_app.dto.ReservaResumenDTO;
import com.try_1.spring.proyect.spring_app.dto.TarifaRutaRequest;
import com.try_1.spring.proyect.spring_app.dto.TarifaRutaResponse;
import com.try_1.spring.proyect.spring_app.models.Reserva;

public interface ReservaService {
    List<Reserva> listar();
    Reserva guardar(Reserva reserva);
    Reserva buscarPorId(Integer id);
    void eliminar(Integer id);
    List<Reserva> buscarPorFechaServicio(LocalDate fechaServicio);
    Reserva actualizar(Integer id, Reserva reserva);
    List<ReservaResumenDTO> buscarResumenPorCliente(Integer idCliente);
    Reserva crearConRuta(CrearReservaRequest request);
    List<HistorialItemDTO> obtenerHistorialPorCliente(Integer idCliente);
    TarifaRutaResponse calcularTarifaRuta(TarifaRutaRequest request);
}
