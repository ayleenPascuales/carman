package com.try_1.spring.proyect.spring_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.try_1.spring.proyect.spring_app.dto.LoginRequest;
import com.try_1.spring.proyect.spring_app.dto.SesionUsuarioDTO;
import com.try_1.spring.proyect.spring_app.services.AuthService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<SesionUsuarioDTO> login(@RequestBody LoginRequest request, HttpSession session) {
        SesionUsuarioDTO sesion = authService.login(request, session);
        if (sesion == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(sesion);
    }

    @GetMapping("/me")
    public ResponseEntity<SesionUsuarioDTO> me(HttpSession session) {
        SesionUsuarioDTO sesion = authService.obtenerSesion(session);
        if (sesion == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(sesion);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        authService.logout(session);
        return ResponseEntity.ok().build();
    }
}
