package com.try_1.spring.proyect.spring_app.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.models.Telemetria;
import com.try_1.spring.proyect.spring_app.repositories.TelemetriaRepository;
import com.try_1.spring.proyect.spring_app.services.TelemetriaService;

@Service
public class TelemetriaServiceImpl implements TelemetriaService{

    @Autowired
    private TelemetriaRepository telemetriaRepository;

    @Override
    public List<Telemetria> listar(){
        return telemetriaRepository.findAll();
    }
    @Override
    public Telemetria guardar(Telemetria telemetria){
        return telemetriaRepository.save(telemetria);
    }
    @Override
    public Telemetria buscarPorId(Integer id){
        return telemetriaRepository.findById(id).orElse(null);
    }
    @Override
    public void eliminar(Integer id){
        telemetriaRepository.deleteById(id);
    }

}
