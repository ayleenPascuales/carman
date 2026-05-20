package com.try_1.spring.proyect.spring_app.services;

import com.try_1.spring.proyect.spring_app.models.Cliente;
import java.util.List;

public interface ClienteService {
    List<Cliente> listar();
    Cliente guardar(Cliente cliente);
    Cliente buscarPorId(Integer id);
    void eliminar(Integer id);
}
