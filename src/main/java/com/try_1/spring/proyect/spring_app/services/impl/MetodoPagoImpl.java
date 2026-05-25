package com.try_1.spring.proyect.spring_app.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.models.MetodoPago;
import com.try_1.spring.proyect.spring_app.repositories.MetodoPagoRepository;
import com.try_1.spring.proyect.spring_app.services.MetodoPagoService;

@Service
public class MetodoPagoImpl implements MetodoPagoService{

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @Override
    public List<MetodoPago> listar(){
        return metodoPagoRepository.findAll(); 
    }
    @Override
    public MetodoPago guardar(MetodoPago metodoPago){
        return metodoPagoRepository.save(metodoPago);
    }
    @Override
    public MetodoPago buscarPorId(Integer id){
        return metodoPagoRepository.findById(id).orElse(null);
    }
    @Override
    public void eliminar(Integer id){
        metodoPagoRepository.deleteById(id);
    }
    @Override
    public List<MetodoPago> buscarPorTipo(String tipo){
        return metodoPagoRepository.findByTipo(tipo);
    }
}
