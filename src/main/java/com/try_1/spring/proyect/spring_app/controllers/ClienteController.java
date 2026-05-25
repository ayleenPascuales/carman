package com.try_1.spring.proyect.spring_app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.try_1.spring.proyect.spring_app.models.Cliente;
import com.try_1.spring.proyect.spring_app.services.ClienteService;

@RestController
@RequestMapping("api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Cliente> listar(){
        return clienteService.listar();
    }
    @GetMapping("/{id}")
    public Cliente buscarPorId(@PathVariable Integer id){
        return clienteService.buscarPorId(id);
    }
    @PostMapping
    public Cliente guardar(@RequestBody Cliente cliente){
        return clienteService.guardar(cliente);
    }
    @PutMapping("/{id}")
    public Cliente actualizar(@PathVariable Integer id, @RequestBody Cliente cliente){
        return clienteService.actualizar(id, cliente);
    }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id){
        clienteService.eliminar(id);
    }
}
