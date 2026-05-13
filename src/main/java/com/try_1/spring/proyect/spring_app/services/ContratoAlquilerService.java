package com.try_1.spring.proyect.spring_app.services;

import java.util.List;
import com.try_1.spring.proyect.spring_app.models.ContratoAlquiler;

public interface ContratoAlquilerService {
    List<ContratoAlquiler> listar();
    ContratoAlquiler guardar(ContratoAlquiler contratoAlquiler);
    ContratoAlquiler buscarPorId(Integer id);
    void eliminar(Integer id);
}
