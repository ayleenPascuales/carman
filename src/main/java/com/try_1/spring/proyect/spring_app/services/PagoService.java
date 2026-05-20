package com.try_1.spring.proyect.spring_app.services;

import java.util.List;
import com.try_1.spring.proyect.spring_app.models.Pago;

public interface PagoService {
    List<Pago> listar();
    Pago guardar(Pago pago);
    Pago buscarPorId(Integer id);
    void eliminar(Integer id);
}
