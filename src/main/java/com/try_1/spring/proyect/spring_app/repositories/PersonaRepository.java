package com.try_1.spring.proyect.spring_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.try_1.spring.proyect.spring_app.models.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Integer>{

    Persona findByEmail(String email);
    Persona findByEmailIgnoreCase(String email);

}
