package com.try_1.spring.proyect.spring_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.try_1.spring.proyect.spring_app.dto.SesionUsuarioDTO;
import com.try_1.spring.proyect.spring_app.services.AuthService;

import jakarta.servlet.http.HttpSession;

@Controller
public class EjemploController {

    @Autowired
    private AuthService authService;

    @GetMapping("/Home")
    public String home() {
        return "Home";
    }

    @GetMapping("/buscar")
    public String buscar() {
        return "buscar";
    }

    @GetMapping("/cliente")
    public String cliente(HttpSession session) {
        if (!esClienteAutenticado(session)) {
            return "redirect:/InicioSesion";
        }
        return "cliente";
    }

    @GetMapping("/conductor")
    public String conductor() {
        return "conductor";
    }

    @GetMapping("/InicioSesion")
    public String inicioSesion(HttpSession session) {
        if (esClienteAutenticado(session)) {
            return "redirect:/cliente";
        }
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

    @GetMapping("/reserva")
    public String reserva(HttpSession session) {
        if (!esClienteAutenticado(session)) {
            return "redirect:/InicioSesion";
        }
        return "reserva";
    }

    private boolean esClienteAutenticado(HttpSession session) {
        SesionUsuarioDTO sesion = authService.obtenerSesion(session);
        return sesion != null && sesion.getIdCliente() != null;
    }
}
