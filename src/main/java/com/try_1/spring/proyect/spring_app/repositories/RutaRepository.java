package com.try_1.spring.proyect.spring_app.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.try_1.spring.proyect.spring_app.models.Ruta;


public interface RutaRepository extends JpaRepository<Ruta, Integer> {

    List<Ruta> findByFecha(LocalDate fecha);
}
