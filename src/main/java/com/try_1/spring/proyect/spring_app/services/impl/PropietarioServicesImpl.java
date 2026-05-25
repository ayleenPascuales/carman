package com.try_1.spring.proyect.spring_app.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.models.Licencia;
import com.try_1.spring.proyect.spring_app.models.Persona;
import com.try_1.spring.proyect.spring_app.models.Propietario;
import com.try_1.spring.proyect.spring_app.repositories.PropietarioRepository;
import com.try_1.spring.proyect.spring_app.services.PropietarioService;

@Service
public class PropietarioServicesImpl implements PropietarioService{

    @Autowired
    private PropietarioRepository propietarioRepository;

    @Override
    public List<Propietario> listar(){
        return propietarioRepository.findAll();
    }
    @Override
    public Propietario guardar(Propietario propietario){
        return propietarioRepository.save(propietario);
    }
    @Override
    public Propietario buscarPorId(Integer id){
        return propietarioRepository.findById(id).orElse(null);
    }
    @Override
    public void eliminar(Integer id){
        propietarioRepository.deleteById(id);
    }
    @Override
    public Propietario actualizar(Integer id, Propietario propietario){
        
        Propietario propietarioExistente = propietarioRepository.findById(id).orElse(null);
        //*PERSONA-USUARIO */
        if(propietarioExistente!= null){
            if (propietario.getPersona()!= null) {
                Persona personaExistente = propietarioExistente.getPersona();
                Persona nuevaPersona = propietario.getPersona();
                
                if(nuevaPersona.getNombre()!=null){
                    personaExistente.setNombre(nuevaPersona.getNombre());
                }
                if(nuevaPersona.getApellido()!= null){
                    personaExistente.setApellido(nuevaPersona.getApellido());
                }
                if(nuevaPersona.getEdad()!= null){
                    personaExistente.setEdad(nuevaPersona.getEdad());
                }
                if(nuevaPersona.getUsuario()!= null){
                    personaExistente.setUsuario(nuevaPersona.getUsuario());
                }
                if(nuevaPersona.getContrasena()!= null){
                    personaExistente.setContrasena(nuevaPersona.getContrasena());
                }
                if(nuevaPersona.getEmail()!= null){
                    personaExistente.setEmail(nuevaPersona.getEmail());
                }
                if(nuevaPersona.getTelefono()!= null){
                    personaExistente.setTelefono(nuevaPersona.getTelefono());
                }
                if(nuevaPersona.getEstado()!=null){
                    personaExistente.setEstado(nuevaPersona.getEstado());
                }
            }
                //*LICENCIA */
                if(propietario.getLicenciaPropietario() != null && propietarioExistente.getLicenciaPropietario() != null){
                Licencia licenciaExistente = propietarioExistente.getLicenciaPropietario();
                Licencia nuevaLicencia = propietario.getLicenciaPropietario();
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
                }      
            return propietarioRepository.save(propietarioExistente);
        }
        return null;     
    }
}


