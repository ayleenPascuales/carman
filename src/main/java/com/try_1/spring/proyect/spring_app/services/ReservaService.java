package com.try_1.spring.proyect.spring_app.services;

import java.util.List;
import com.try_1.spring.proyect.spring_app.models.Reserva;

public interface ReservaService {
    List<Reserva> listar();
    Reserva guardar(Reserva reserva);
    Reserva buscarPorId(Integer id);
    void eliminar(Integer id);
}
