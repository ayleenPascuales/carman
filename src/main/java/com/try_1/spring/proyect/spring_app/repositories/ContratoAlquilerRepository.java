package com.try_1.spring.proyect.spring_app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.try_1.spring.proyect.spring_app.models.ContratoAlquiler;

public interface ContratoAlquilerRepository extends JpaRepository<ContratoAlquiler, Integer>{

    List<ContratoAlquiler> findByCliente_IdCliente(Integer idCliente);
    ContratoAlquiler findByReserva_IdReserva(Integer idReserva);

}
