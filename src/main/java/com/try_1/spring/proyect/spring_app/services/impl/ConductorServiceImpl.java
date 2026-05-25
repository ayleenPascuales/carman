package com.try_1.spring.proyect.spring_app.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.models.Conductor;
import com.try_1.spring.proyect.spring_app.models.Licencia;
import com.try_1.spring.proyect.spring_app.models.Persona;
import com.try_1.spring.proyect.spring_app.repositories.ConductorRepository;
import com.try_1.spring.proyect.spring_app.services.ConductorService;

@Service
public class ConductorServiceImpl implements ConductorService{

    @Autowired
    private ConductorRepository conductorRepository;

    @Override
    public List<Conductor> listar(){
        return conductorRepository.findAll();
    }

    @Override
    public Conductor guardar(Conductor conductor){
        return conductorRepository.save(conductor);
    }

    @Override
    public Conductor buscarPorId(Integer id){
        return conductorRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Integer id){
        conductorRepository.deleteById(id);
    }
    @Override
    public Conductor actualizar(Integer id, Conductor conductor){

        Conductor conductorExistente = conductorRepository.findById(id).orElse(null);
        //*PERSONA/USUARIO */
        if(conductorExistente != null){
            if(conductor.getPersona() != null){
                Persona personaExistente = conductorExistente.getPersona();
                Persona nuevaPersona = conductor.getPersona();

                if(nuevaPersona.getNombre() != null){
                    personaExistente.setNombre(nuevaPersona.getNombre());
                }
                if(nuevaPersona.getApellido() != null){
                    personaExistente.setApellido(nuevaPersona.getApellido());
                }
                if(nuevaPersona.getEdad()!= null){
                    personaExistente.setEdad(nuevaPersona.getEdad());
                }
                if(nuevaPersona.getUsuario()!=null){
                    personaExistente.setUsuario(nuevaPersona.getUsuario());
                }
                if(nuevaPersona.getContrasena()!=null){
                    personaExistente.setContrasena(nuevaPersona.getContrasena());
                }
                if(nuevaPersona.getEmail()!=null){
                    personaExistente.setEmail(nuevaPersona.getEmail());
                }
                if(nuevaPersona.getTelefono()!= null){
                    personaExistente.setTelefono(nuevaPersona.getTelefono());
                }
                if(nuevaPersona.getEstado()!=null){
                    personaExistente.setEstado(nuevaPersona.getEstado());
                }
                //*LICENCIA */
            }
            if(conductor.getLicenciaConductor() != null && conductorExistente.getLicenciaConductor() != null){
                Licencia licenciaExistente = conductorExistente.getLicenciaConductor();
                Licencia nuevaLicencia = conductor.getLicenciaConductor();
                if(nuevaLicencia.getNumero()!= null){
                licenciaExistente.setNumero(nuevaLicencia.getNumero());
                }
                if(nuevaLicencia.getFechaExpiracion()!= null){
                licenciaExistente.setFechaExpiracion(nuevaLicencia.getFechaExpiracion());
                }
                if(nuevaLicencia.getEstado()!= null){
                licenciaExistente.setEstado(nuevaLicencia.getEstado());
                }
                if(nuevaLicencia.getTipoLicencia()!=null){
                licenciaExistente.setTipoLicencia(nuevaLicencia.getTipoLicencia());
                }
                if(nuevaLicencia.getFoto()!=null){
                licenciaExistente.setFoto(nuevaLicencia.getFoto());
                }
                //*DISPONIBILIDAD */
            }
            if(conductor.getDisponibilidad() != null) {
                conductorExistente.setDisponibilidad(conductor.getDisponibilidad());
            }
                //*CALIFICACION */
            if(conductor.getCalificacion() != null) {
                conductorExistente.setCalificacion(conductor.getCalificacion());
            }
        return conductorRepository.save(conductorExistente);  
        }
    return null;
    }
}

