package com.try_1.spring.proyect.spring_app.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.try_1.spring.proyect.spring_app.models.MetodoPago;

public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Integer> {

    List<MetodoPago> findByTipo(String tipo);
}
