package com.try_1.spring.proyect.spring_app.services;

import java.util.List;

import com.try_1.spring.proyect.spring_app.dto.VehiculoCatalogoDTO;
import com.try_1.spring.proyect.spring_app.models.Vehiculo;

public interface VehiculoService {
    List<Vehiculo> listar();
    Vehiculo guardar(Vehiculo vehiculo);
    Vehiculo buscarPorId(Integer id);
    void eliminar(Integer id);
    Vehiculo buscarPorPlaca(String placa);
    void eliminarPorPlaca(String placa);
    Vehiculo buscarPorMarca(String marca);
    Vehiculo buscarPorModelo(String modelo);
    Vehiculo actualizar(String placa, Vehiculo vehiculo);
    java.util.List<VehiculoCatalogoDTO> listarCatalogo();

}

