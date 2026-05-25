package com.try_1.spring.proyect.spring_app.services;

import java.util.List;

import com.try_1.spring.proyect.spring_app.models.Conductor;

public interface ConductorService {
    List<Conductor> listar();
    Conductor guardar(Conductor conductor);
    Conductor buscarPorId(Integer id);
    void eliminar(Integer id);
    Conductor actualizar(Integer id, Conductor conductor);
}
