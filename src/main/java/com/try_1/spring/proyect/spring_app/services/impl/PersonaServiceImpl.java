package com.try_1.spring.proyect.spring_app.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.models.Persona;
import com.try_1.spring.proyect.spring_app.repositories.PersonaRepository;
import com.try_1.spring.proyect.spring_app.services.PersonaService;

@Service
public class PersonaServiceImpl implements PersonaService{

    @Autowired
    private PersonaRepository personaRepository;

    @Override
    public List<Persona> listar(){
        return personaRepository.findAll();
    }

    @Override
    public Persona guardar(Persona persona){
        return personaRepository.save(persona);
    }

    @Override
    public Persona buscarPorId(Integer id){
        return personaRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Integer id){
        personaRepository.deleteById(id);
    }
    @Override
    public Persona actualizar(Integer id, Persona persona){
        Persona personaExistente = personaRepository.findById(id).orElse(null);

        if(personaExistente != null){
            if(persona.getNombre()!= null){
                personaExistente.setNombre(persona.getNombre());
            }
            if(persona.getApellido()!= null){
                personaExistente.setApellido(persona.getApellido());
            }
            if(persona.getEdad()!=null){
                personaExistente.setEdad(persona.getEdad());
            }
            if(persona.getUsuario()!= null){
                personaExistente.setUsuario(persona.getUsuario());
            }
            if(persona.getContrasena()!= null){
                personaExistente.setContrasena(persona.getContrasena());
            }
            if(persona.getEmail() != null){
                personaExistente.setEmail(persona.getContrasena());
            }
            if(persona.getTelefono()!= null){
                personaExistente.setTelefono(persona.getTelefono());
            }
            if(persona.getEstado() != null){
                personaExistente.setEstado(persona.getEstado());
            }
            return personaRepository.save(personaExistente);
        }
        return null;   
    }

}
