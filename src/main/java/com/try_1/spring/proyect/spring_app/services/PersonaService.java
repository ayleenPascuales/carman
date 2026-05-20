package com.try_1.spring.proyect.spring_app.services;

import com.try_1.spring.proyect.spring_app.models.Persona;
import java.util.List;

public interface PersonaService {
    List<Persona> listar();
    Persona guardar(Persona persona);
    Persona buscarPorId(Integer id);
    void eliminar(Integer id);
}
