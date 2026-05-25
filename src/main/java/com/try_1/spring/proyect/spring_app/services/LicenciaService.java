package com.try_1.spring.proyect.spring_app.services;

import java.util.List;

import com.try_1.spring.proyect.spring_app.models.Licencia;

public interface LicenciaService {
    
    List<Licencia> listar();
    Licencia guardar(Licencia licencia);
    Licencia buscarPorId(Integer id);
    void eliminar(Integer id); 
    Licencia buscarPorNumero(String Numero);
    void eliminarPorNumero(String Numero);
}
