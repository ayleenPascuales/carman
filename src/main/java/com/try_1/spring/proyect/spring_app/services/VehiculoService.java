package com.try_1.spring.proyect.spring_app.services;

import java.util.*;
import com.try_1.spring.proyect.spring_app.models.Vehiculo;

public interface VehiculoService {
    List<Vehiculo> listar();
    Vehiculo guardar(Vehiculo vehiculo);
    Vehiculo buscarPorId(Integer id);
    void eliminar(Integer id);

}

