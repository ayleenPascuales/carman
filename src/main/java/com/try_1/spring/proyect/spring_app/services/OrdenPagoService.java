package com.try_1.spring.proyect.spring_app.services;

import com.try_1.spring.proyect.spring_app.models.OrdenPago;
import java.util.List;

public interface OrdenPagoService {
    List<OrdenPago> listar();
    OrdenPago guardar(OrdenPago ordenPago);
    OrdenPago buscarPorId(Integer id);
    void eliminar(Integer id);
}
