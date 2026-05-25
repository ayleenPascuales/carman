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

import com.try_1.spring.proyect.spring_app.models.Licencia;
import com.try_1.spring.proyect.spring_app.services.LicenciaService;

@RestController
@RequestMapping("api/licencias")
public class LicenciaController {

    @Autowired
    private LicenciaService licenciaService;

    @GetMapping
    public List<Licencia> listar(){
        return licenciaService.listar();
    }
    @GetMapping("/{id}")
    public Licencia buscarPorId(@PathVariable Integer id){
        return licenciaService.buscarPorId(id);
    }
    @PostMapping
    public Licencia guardar(@RequestBody Licencia licencia){
        return licenciaService.guardar(licencia);
    }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id){
        licenciaService.eliminar(id);
    }
    @GetMapping("/numero/{numero}")
    public Licencia buscarPorNumero(@PathVariable String numero){
        return licenciaService.buscarPorNumero(numero);
    }
    @DeleteMapping("/numero/{numero}")
    public void eliminarPorNumero(@PathVariable String numero){
        licenciaService.eliminarPorNumero(numero);
    }
}
