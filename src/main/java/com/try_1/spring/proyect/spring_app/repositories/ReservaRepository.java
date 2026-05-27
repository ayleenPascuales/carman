package com.try_1.spring.proyect.spring_app.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.try_1.spring.proyect.spring_app.models.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Integer>{

    List<Reserva> findByFechaServicio(LocalDate fechaServicio);
    List<Reserva> findByCliente_IdCliente(Integer idCliente);

}
