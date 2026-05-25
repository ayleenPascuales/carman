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

import com.try_1.spring.proyect.spring_app.models.EstadoTecnicoVehiculo;
import com.try_1.spring.proyect.spring_app.services.EstadoTecnicoVehiculoService;

@RestController
@RequestMapping("api/estadoVehiculo")
public class EstadoTecnicoVehiculoController {

    @Autowired
    private EstadoTecnicoVehiculoService estadoTecnicoVehiculoService;

    @GetMapping
    public List<EstadoTecnicoVehiculo> listar(){
        return estadoTecnicoVehiculoService.listar();
    }
    @GetMapping("/{id}")
    public EstadoTecnicoVehiculo buscarPorId(@PathVariable Integer id){
        return estadoTecnicoVehiculoService.buscarPorId(id);
    }
    @PostMapping
    public EstadoTecnicoVehiculo guardar(@RequestBody EstadoTecnicoVehiculo estadoTecnicoVehiculo){
        return estadoTecnicoVehiculoService.guardar(estadoTecnicoVehiculo);
    }
    @PutMapping("/{id}")
    public EstadoTecnicoVehiculo actualizar(@PathVariable Integer id, @RequestBody EstadoTecnicoVehiculo estadoTecnicoVehiculo){
        return estadoTecnicoVehiculoService.actualizar(id, estadoTecnicoVehiculo);
    }
    @DeleteMapping("/{id}")
    public void elimianr(Integer id){
        estadoTecnicoVehiculoService.eliminar(id);
    }
}
