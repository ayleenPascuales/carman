package com.try_1.spring.proyect.spring_app.services;

import java.util.List;

import com.try_1.spring.proyect.spring_app.models.MetodoPago;

public interface MetodoPagoService {
    List<MetodoPago> listar();
    MetodoPago guardar(MetodoPago metodoPago);
    MetodoPago buscarPorId(Integer id);
    void eliminar(Integer id);
    List<MetodoPago> buscarPorTipo(String tipo);
    List<MetodoPago> listarActivos();
}
