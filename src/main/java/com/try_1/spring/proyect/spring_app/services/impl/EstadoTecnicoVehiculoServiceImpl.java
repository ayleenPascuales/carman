package com.try_1.spring.proyect.spring_app.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.models.EstadoTecnicoVehiculo;
import com.try_1.spring.proyect.spring_app.models.EstadoVehiculo;
import com.try_1.spring.proyect.spring_app.models.Vehiculo;
import com.try_1.spring.proyect.spring_app.repositories.EstadoTecnicoVehiculoRepository;
import com.try_1.spring.proyect.spring_app.services.EstadoTecnicoVehiculoService;

@Service
public class EstadoTecnicoVehiculoServiceImpl implements EstadoTecnicoVehiculoService{

    @Autowired
    private EstadoTecnicoVehiculoRepository estadoTecnicoVehiculoRepository;

    @Override
    public List<EstadoTecnicoVehiculo> listar(){
        return estadoTecnicoVehiculoRepository.findAll();
    } 
    @Override
    public EstadoTecnicoVehiculo guardar(EstadoTecnicoVehiculo estadoTecnicoVehiculo){
        return estadoTecnicoVehiculoRepository.save(estadoTecnicoVehiculo);
    }
    @Override
    public EstadoTecnicoVehiculo buscarPorId(Integer id){
        return estadoTecnicoVehiculoRepository.findById(id).orElse(null);
    }
    @Override
    public void eliminar(Integer id){
        estadoTecnicoVehiculoRepository.deleteById(id);
    }
    @Override
    public EstadoTecnicoVehiculo actualizar(Integer id, EstadoTecnicoVehiculo estadoTecnicoVehiculo){

        EstadoTecnicoVehiculo estado = estadoTecnicoVehiculoRepository.findById(id).orElse(null);

        if(estado != null){
            if(estadoTecnicoVehiculo.getKilometraje()!= null){
                estado.setKilometraje(estadoTecnicoVehiculo.getKilometraje());
            }
            if(estadoTecnicoVehiculo.getEstadoMotor()!= null){
                estado.setEstadoMotor(estadoTecnicoVehiculo.getEstadoMotor());
            }
            if(estadoTecnicoVehiculo.getEstadoFrenos()!= null){
                estado.setEstadoFrenos(estadoTecnicoVehiculo.getEstadoFrenos());
            }
            if(estadoTecnicoVehiculo.getEstadoLlantas()!= null){
                estado.setEstadoLlantas(estadoTecnicoVehiculo.getEstadoLlantas());
            }
            if(estadoTecnicoVehiculo.getEstadoBateria()!= null){
                estado.setEstadoBateria(estadoTecnicoVehiculo.getEstadoBateria());
            }
            if(estadoTecnicoVehiculo.getNecesitaMantenimiento()!= null){
                estado.setNecesitaMantenimiento(estadoTecnicoVehiculo.getNecesitaMantenimiento());
            }
            
            Vehiculo vehiculo = estado.getVehiculo();

            if(estado.getNecesitaMantenimiento()){

                vehiculo.setEstado(EstadoVehiculo.EN_MANTENIMIENTO);

            }else{
                vehiculo.setEstado(EstadoVehiculo.ACTIVO);
        }
        return estadoTecnicoVehiculoRepository.save(estado);
        }
        return null;
    }   
}
