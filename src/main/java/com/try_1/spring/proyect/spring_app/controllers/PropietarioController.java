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

import com.try_1.spring.proyect.spring_app.models.Propietario;
import com.try_1.spring.proyect.spring_app.services.PropietarioService;

@RestController
@RequestMapping("api/propietarios")
public class PropietarioController {

    @Autowired
    private PropietarioService propietarioService;

    @GetMapping
    public List<Propietario> listar(){
        return propietarioService.listar();
    }
    @GetMapping("/{id}")
    public Propietario buscarPorId(@PathVariable Integer id){
        return propietarioService.buscarPorId(id);
    }
    @PostMapping
    public Propietario guardar(@RequestBody Propietario propietario){
        return propietarioService.guardar(propietario);
    }
    @PutMapping("/{id}")
    public Propietario actualizar(@PathVariable Integer id, @RequestBody Propietario propietario){
        return propietarioService.actualizar(id, propietario);
    }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id){
        propietarioService.eliminar(id);
    }
}
