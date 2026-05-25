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

import com.try_1.spring.proyect.spring_app.models.Notificacion;
import com.try_1.spring.proyect.spring_app.services.NotificacionService;

@RestController
@RequestMapping("api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping
    public List<Notificacion> listar(){
        return notificacionService.listar();
    }
    @GetMapping("/{id}")
    public Notificacion buscarPorId(@PathVariable Integer id){
        return notificacionService.buscarPorId(id);
    }
    @PostMapping
    public Notificacion guardar(@RequestBody Notificacion notificacion){
        return notificacionService.guardar(notificacion);
    }
    @PutMapping("/{id}")
    public Notificacion actualizar(@PathVariable Integer id, @RequestBody Notificacion notificacion){
        return notificacionService.actualizar(id, notificacion);
    }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id){
        notificacionService.eliminar(id);
    }
}
