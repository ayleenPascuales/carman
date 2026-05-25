package com.try_1.spring.proyect.spring_app.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.models.Notificacion;
import com.try_1.spring.proyect.spring_app.repositories.NotificacionRepository;
import com.try_1.spring.proyect.spring_app.services.NotificacionService;


@Service
public class NotificacionServicesImpl implements NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Override
    public List<Notificacion> listar(){
        return notificacionRepository.findAll();
    }
    @Override
    public Notificacion guardar(Notificacion notificacion){
        notificacion.setFechaHoraEnvio(LocalDateTime.now());
        notificacion.setLeida(false);
        return notificacionRepository.save(notificacion);
    }
    @Override
    public Notificacion buscarPorId(Integer id){
        return notificacionRepository.findById(id).orElse(null);
    }
    @Override
    public void eliminar(Integer id){
        notificacionRepository.deleteById(id);
    }
    @Override
    public Notificacion actualizar(Integer id, Notificacion notificacion){
        Notificacion notificacionExistente =notificacionRepository.findById(id).orElse(null);

        if(notificacionExistente != null){

            if(notificacion.getMensaje()!= null){
                notificacionExistente.setMensaje(notificacion.getMensaje());
        }

        if(notificacion.getLeida()!= null){
            notificacionExistente.setLeida(notificacion.getLeida());
        }
        return notificacionRepository.save(notificacionExistente);
        }
        return null;
    }

}
