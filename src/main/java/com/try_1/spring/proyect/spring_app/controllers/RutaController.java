package com.try_1.spring.proyect.spring_app.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.try_1.spring.proyect.spring_app.models.Ruta;
import com.try_1.spring.proyect.spring_app.services.RutaService;

@RestController
@RequestMapping("api/rutas")
public class RutaController {

    @Autowired
    private RutaService rutaService;

    @GetMapping
    public List<Ruta> listar(){
        return rutaService.listar();
    }
    @GetMapping("/{id}")
    public Ruta buscarPorId(@PathVariable Integer id){
        return rutaService.buscarPorId(id);
    }
    @PostMapping
    public Ruta guardar(@RequestBody Ruta ruta){
        return rutaService.guardar(ruta);
    }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id){
        rutaService.eliminar(id);
    }
    @PostMapping("/fecha")
    public List<Ruta> buscarPorFecha(@RequestParam LocalDate fecha){
        return rutaService.buscarPorFecha(fecha);
    }
}
