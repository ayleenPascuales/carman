package com.try_1.spring.proyect.spring_app.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.models.Cliente;
import com.try_1.spring.proyect.spring_app.models.Licencia;
import com.try_1.spring.proyect.spring_app.models.Persona;
import com.try_1.spring.proyect.spring_app.repositories.ClienteRepository;
import com.try_1.spring.proyect.spring_app.services.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public List <Cliente> listar(){
        return clienteRepository.findAll();
    }

    @Override
    public Cliente guardar(Cliente cliente){
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente buscarPorId(Integer id){
        return clienteRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Integer id){
        clienteRepository.deleteById(id);
    }
    @Override
    public Cliente actualizar(Integer id, Cliente cliente){

        Cliente clienteExistente = clienteRepository.findById(id).orElse(null);
        //*PERSONA/USUARIO */
        if(clienteExistente != null){
            if(cliente.getPersona() != null){
                Persona personaExistente = clienteExistente.getPersona();
                Persona nuevaPersona = cliente.getPersona();

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
            if(cliente.getLicenciaCliente() != null && clienteExistente.getLicenciaCliente() != null){
                Licencia licenciaExistente = clienteExistente.getLicenciaCliente();
                Licencia nuevaLicencia = cliente.getLicenciaCliente();

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
        return clienteRepository.save(clienteExistente);  
        }
    return null;
    }
}
