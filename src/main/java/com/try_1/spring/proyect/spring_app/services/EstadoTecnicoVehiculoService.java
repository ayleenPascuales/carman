package com.try_1.spring.proyect.spring_app.services;

import java.util.List;

import com.try_1.spring.proyect.spring_app.models.EstadoTecnicoVehiculo;

public interface EstadoTecnicoVehiculoService {
    List<EstadoTecnicoVehiculo> listar();
    EstadoTecnicoVehiculo guardar(EstadoTecnicoVehiculo estadoTecnicoVehiculo);
    EstadoTecnicoVehiculo buscarPorId(Integer id);
    void eliminar(Integer id);
    public EstadoTecnicoVehiculo actualizar(Integer id, EstadoTecnicoVehiculo estadoTecnicoVehiculo);
}
