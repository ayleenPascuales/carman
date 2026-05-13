package com.try_1.spring.proyect.spring_app.services;

import com.try_1.spring.proyect.spring_app.models.Conductor;
import java.util.List;

public interface ConductorService {
    List<Conductor> listar();
    Conductor guardar(Conductor conductor);
    Conductor buscarPorId(Integer id);
    void eliminar(Integer id);
}
