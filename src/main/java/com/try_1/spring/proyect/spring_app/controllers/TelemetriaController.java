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

import com.try_1.spring.proyect.spring_app.models.Telemetria;
import com.try_1.spring.proyect.spring_app.services.TelemetriaService;

@RestController
@RequestMapping("api/telemetrias")
public class TelemetriaController {

    @Autowired
    private TelemetriaService telemetriaService;

    @GetMapping
    public List<Telemetria> listar(){
        return telemetriaService.listar();
    }
    @GetMapping("/{id}")
    public Telemetria buscarPorId(@PathVariable Integer id){
        return telemetriaService.buscarPorId(id);
    }
    @PostMapping
    public Telemetria guardar(@RequestBody Telemetria telemetria){
        return telemetriaService.guardar(telemetria);
    }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id){
        telemetriaService.eliminar(id);
    }
}
