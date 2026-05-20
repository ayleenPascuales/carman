package com.try_1.spring.proyect.spring_app.services;

import java.util.List;
import com.try_1.spring.proyect.spring_app.models.Ruta;

public interface RutaService {
    List<Ruta> listar();
    Ruta guardar(Ruta ruta);
    Ruta buscarPorId(Integer id);
    void eliminar(Integer id);
}
