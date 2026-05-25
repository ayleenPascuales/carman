package com.try_1.spring.proyect.spring_app.services;

import java.util.List;

import com.try_1.spring.proyect.spring_app.models.Cliente;

public interface ClienteService {
    List<Cliente> listar();
    Cliente guardar(Cliente cliente);
    Cliente buscarPorId(Integer id);
    void eliminar(Integer id);
    Cliente actualizar(Integer id, Cliente cliente);
}
