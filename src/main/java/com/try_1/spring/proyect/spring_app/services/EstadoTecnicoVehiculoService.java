package com.try_1.spring.proyect.spring_app.services;

import com.try_1.spring.proyect.spring_app.models.EstadoTecnicoVehiculo;
import java.util.*;

public interface EstadoTecnicoVehiculoService {
    List<EstadoTecnicoVehiculo> listar();
    EstadoTecnicoVehiculo guardar(EstadoTecnicoVehiculo estadoTecnicoVehiculo);
    EstadoTecnicoVehiculo buscarPorId(Integer id);
    void eliminar(Integer id);
}
