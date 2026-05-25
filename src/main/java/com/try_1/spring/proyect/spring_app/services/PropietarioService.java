package com.try_1.spring.proyect.spring_app.services;


import java.util.List;

import com.try_1.spring.proyect.spring_app.models.Propietario;

public interface PropietarioService {
    List<Propietario> listar();
    Propietario guardar(Propietario propietario);
    Propietario buscarPorId(Integer id);
    void eliminar(Integer id);
    Propietario actualizar(Integer id, Propietario propietario);
}
