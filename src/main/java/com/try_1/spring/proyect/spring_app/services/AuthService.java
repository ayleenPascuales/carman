package com.try_1.spring.proyect.spring_app.services;

import com.try_1.spring.proyect.spring_app.dto.LoginRequest;
import com.try_1.spring.proyect.spring_app.dto.SesionUsuarioDTO;

import jakarta.servlet.http.HttpSession;

public interface AuthService {
    SesionUsuarioDTO login(LoginRequest request, HttpSession session);
    SesionUsuarioDTO obtenerSesion(HttpSession session);
    void logout(HttpSession session);
}
