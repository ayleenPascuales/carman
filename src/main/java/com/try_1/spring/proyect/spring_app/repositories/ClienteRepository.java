package com.try_1.spring.proyect.spring_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.try_1.spring.proyect.spring_app.models.Cliente;


public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

    Cliente findByPersona_IdPersona(Integer idPersona);

}
