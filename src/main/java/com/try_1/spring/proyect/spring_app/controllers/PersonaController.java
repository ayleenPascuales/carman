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

import com.try_1.spring.proyect.spring_app.models.Persona;
import com.try_1.spring.proyect.spring_app.services.PersonaService;

@RestController
@RequestMapping("api/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @GetMapping
    public List<Persona> listar(){
        return personaService.listar();
    }
    @GetMapping("/{id}")
    public Persona buscarPorId(@PathVariable Integer id){
        return personaService.buscarPorId(id);
    }
    @PostMapping
    public Persona guardar(@RequestBody Persona persona){
        return personaService.guardar(persona);
    }
    @PutMapping("/{id}")
    public Persona actualizar(@PathVariable Integer id, @RequestBody Persona persona){
        return personaService.actualizar(id, persona);
    }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id){
        personaService.eliminar(id);
    }
}
