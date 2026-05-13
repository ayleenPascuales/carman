package com.try_1.spring.proyect.spring_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.try_1.spring.proyect.spring_app.models.Conductor;


public interface ConductorRepository extends JpaRepository<Conductor, Integer>{

}
