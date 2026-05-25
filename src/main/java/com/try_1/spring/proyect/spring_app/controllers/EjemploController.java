package com.try_1.spring.proyect.spring_app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class EjemploController {
    
    @GetMapping("/Home")
    public String home() {
        return "Home";
    }

    @GetMapping("/buscar")
    public String buscar() {
        return "buscar";
    }

    @GetMapping("/cliente")
    public String cliente() {
        return "cliente";
    }

    @GetMapping("/conductor")
    public String conductor() {
        return "conductor";
    }

    @GetMapping("/InicioSesion")
    public String inicioSesion() {
        return "InicioSesion";
    }

    @GetMapping("/Propietario")
    public String propietario() {
        return "Propietario";
    }

    @GetMapping("/registro2")
    public String registro2() {
        return "registro2";
    }

    
}