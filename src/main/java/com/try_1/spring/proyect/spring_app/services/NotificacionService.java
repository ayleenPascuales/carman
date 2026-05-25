package com.try_1.spring.proyect.spring_app.services;

import java.util.List;

import com.try_1.spring.proyect.spring_app.models.Notificacion;

public interface NotificacionService {
    List<Notificacion> listar();
    Notificacion guardar(Notificacion notificacion);
    Notificacion buscarPorId(Integer id);
    void eliminar(Integer id);
    Notificacion actualizar(Integer id,Notificacion notificacion);
}
