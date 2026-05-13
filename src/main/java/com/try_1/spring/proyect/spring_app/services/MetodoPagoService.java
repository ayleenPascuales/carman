package com.try_1.spring.proyect.spring_app.services;

import com.try_1.spring.proyect.spring_app.models.MetodoPago;
import java.util.List;

public interface MetodoPagoService {
    List<MetodoPago> listar();
    MetodoPago guardar(MetodoPago metodoPago);
    MetodoPago buscarPorId(Integer id);
    void eliminar(Integer id);
}
