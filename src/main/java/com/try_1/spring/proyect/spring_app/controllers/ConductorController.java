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

import com.try_1.spring.proyect.spring_app.models.Conductor;
import com.try_1.spring.proyect.spring_app.services.ConductorService;

@RestController
@RequestMapping("api/conductor")
public class ConductorController {

    @Autowired
    private ConductorService conductorService;

    @GetMapping
    public List<Conductor> listar(){
        return conductorService.listar();
    }
    @GetMapping("/{id}")
    public Conductor buscarPorId(@PathVariable Integer id){
        return conductorService.buscarPorId(id);
    }
    @PostMapping
    public Conductor guardar(@RequestBody Conductor conductor){
        return conductorService.guardar(conductor);
    }
    @PutMapping("/{id}")
    public Conductor actualizar(@PathVariable Integer id, @RequestBody Conductor conductor){
        return conductorService.actualizar(id, conductor);
    }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id){
        conductorService.eliminar(id);
    }


}


