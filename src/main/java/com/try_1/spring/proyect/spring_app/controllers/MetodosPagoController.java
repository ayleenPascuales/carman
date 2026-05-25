package com.try_1.spring.proyect.spring_app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.try_1.spring.proyect.spring_app.models.MetodoPago;
import com.try_1.spring.proyect.spring_app.services.MetodoPagoService;

@RestController
@RequestMapping("api/metodosPago")
public class MetodosPagoController {

    @Autowired
    private MetodoPagoService metodoPagoService;

    @GetMapping
    public List<MetodoPago> listar(){
        return metodoPagoService.listar();
    }
    @GetMapping("/{id}")
    public MetodoPago buscarPorId(@PathVariable Integer id){
        return metodoPagoService.buscarPorId(id);
    }
    @PostMapping
    public MetodoPago guardar(@RequestBody MetodoPago metodoPago){
        return metodoPagoService.guardar(metodoPago);
    }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id){
        metodoPagoService.eliminar(id);
    }
    @GetMapping("tipo/{tipo}")
    public List<MetodoPago> buscarPorTipo(@PathVariable String tipo){
        return metodoPagoService.buscarPorTipo(tipo);
    }
}
