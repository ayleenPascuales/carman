package com.try_1.spring.proyect.spring_app.services;


import com.try_1.spring.proyect.spring_app.models.Propietario;
import java.util.List;

public interface PropietarioService {
    List<Propietario> listar();
    Propietario guardar(Propietario propietario);
    Propietario buscarPorId(Integer id);
    void eliminar(Integer id);
}
