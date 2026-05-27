package com.try_1.spring.proyect.spring_app.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.try_1.spring.proyect.spring_app.models.Pago;

public interface PagoRepository extends JpaRepository<Pago, Integer>{

    List<Pago> findByFechaHoraPago(LocalDateTime fechaHoraPago);
    List<Pago> findByReserva_IdReserva(Integer idReserva);
    List<Pago> findByReserva_Cliente_IdCliente(Integer idCliente);
}
