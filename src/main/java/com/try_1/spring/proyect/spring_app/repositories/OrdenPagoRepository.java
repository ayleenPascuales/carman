package com.try_1.spring.proyect.spring_app.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.try_1.spring.proyect.spring_app.models.OrdenPago;
import com.try_1.spring.proyect.spring_app.models.Reserva;


public interface OrdenPagoRepository extends JpaRepository<OrdenPago, Integer> {

    List<OrdenPago> findByFechaEmision(LocalDate fechaEmision);
    List<OrdenPago> findByReserva(Reserva reserva);
    List<OrdenPago> findByReserva_IdReserva(Integer idReserva);
    List<OrdenPago> findByReserva_Cliente_IdCliente(Integer idCliente);
}
