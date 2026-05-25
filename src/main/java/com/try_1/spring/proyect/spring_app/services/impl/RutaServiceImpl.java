package com.try_1.spring.proyect.spring_app.services.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.models.Ruta;
import com.try_1.spring.proyect.spring_app.repositories.RutaRepository;
import com.try_1.spring.proyect.spring_app.services.RutaService;

@Service
public class RutaServiceImpl implements RutaService {

    @Autowired
    private RutaRepository rutaRepository;

    @Override
    public List<Ruta> listar(){
        return rutaRepository.findAll();
    }
    @Override
    public Ruta guardar(Ruta ruta){
        return rutaRepository.save(ruta);
    } 
    @Override
    public Ruta buscarPorId(Integer id){
        return rutaRepository.findById(id).orElse(null);
    }
    @Override
    public void eliminar(Integer id){
        rutaRepository.deleteById(id);
    }
    @Override
    public List<Ruta> buscarPorFecha(LocalDate fecha){
        return rutaRepository.findByFecha(fecha);
    }   
}
