package com.try_1.spring.proyect.spring_app.services;

import java.util.List;

import com.try_1.spring.proyect.spring_app.models.Persona;

public interface PersonaService {
    List<Persona> listar();
    Persona guardar(Persona persona);
    Persona buscarPorId(Integer id);
    void eliminar(Integer id);
    Persona actualizar(Integer id, Persona persona);
}
