package com.try_1.spring.proyect.spring_app.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.models.Licencia;
import com.try_1.spring.proyect.spring_app.repositories.LicenciaRepository;
import com.try_1.spring.proyect.spring_app.services.LicenciaService;

@Service
public class LicenciaServiceImpl implements LicenciaService{

    @Autowired
    private LicenciaRepository licenciaRepository;

    @Override
    public List <Licencia> listar(){
        return licenciaRepository.findAll();
    }

    @Override
    public Licencia guardar(Licencia licencia){
        return licenciaRepository.save(licencia);
    }

    @Override
    public Licencia buscarPorId(Integer id){
        return licenciaRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Integer numero){
        licenciaRepository.deleteById(numero);
    }
    @Override
    public Licencia buscarPorNumero(String Numero){
        return licenciaRepository.findbyNumero(Numero);
    }
    @Override
    public void eliminarPorNumero(String Numero){
        licenciaRepository.deleteByNumero(Numero);
    }

}
