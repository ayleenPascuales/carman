package com.try_1.spring.proyect.spring_app.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.models.Notificacion;
import com.try_1.spring.proyect.spring_app.models.TipoNotificacion;
import com.try_1.spring.proyect.spring_app.models.Vehiculo;
import com.try_1.spring.proyect.spring_app.repositories.VehiculoRepository;
import com.try_1.spring.proyect.spring_app.services.NotificacionService;
import com.try_1.spring.proyect.spring_app.services.VehiculoService;;

@Service
public class VehiculoServiceImpl implements VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;
    @Autowired
    private NotificacionService notificacionService;

    @Override
    public List<Vehiculo> listar(){
        return vehiculoRepository.findAll();
    }
    @Override
    public Vehiculo guardar(Vehiculo vehiculo){
        
        Vehiculo vehiculoGuardado = vehiculoRepository.save(vehiculo);
        Notificacion n =new Notificacion();
        n.setTipo(TipoNotificacion.VEHICULO);
        n.setMensaje("Vehículo registrado: "+ vehiculoGuardado.getPlaca());
        notificacionService.guardar(n);
        return vehiculoGuardado;
    }
    @Override
    public Vehiculo buscarPorId(Integer id){
        return vehiculoRepository.findById(id).orElse(null);
    }
    @Override
    public void eliminar(Integer id){
        vehiculoRepository.deleteById(id);
    }
    @Override
    public Vehiculo buscarPorPlaca(String Placa){
        return vehiculoRepository.findByPlaca(Placa);
    }
    @Override
    public void eliminarPorPlaca(String placa){
        vehiculoRepository.deleteByPlaca(placa);
    }
    @Override
    public Vehiculo buscarPorMarca(String marca){
        return vehiculoRepository.findByMarca(marca);
    }
    @Override
    public Vehiculo buscarPorModelo(String modelo){
        return vehiculoRepository.findByModelo(modelo);
    }
    @Override
    public Vehiculo actualizar(String placa, Vehiculo vehiculo){
        
        Vehiculo vehiculoExistente = vehiculoRepository.findByPlaca(placa);

        if(vehiculoExistente != null){
            if(vehiculo.getMarca()!= null){
                vehiculoExistente.setMarca(vehiculo.getMarca());
            }
            if(vehiculo.getModelo()!= null){
                vehiculoExistente.setModelo(vehiculo.getModelo());
            }
            if(vehiculo.getAnio()!= null){
                vehiculoExistente.setAnio(vehiculo.getAnio());
            }
            if(vehiculo.getCapacidad()!= null){
                vehiculoExistente.setCapacidad(vehiculo.getCapacidad());
            }
            if(vehiculo.getTipoVehiculo()!= null){
                vehiculoExistente.setTipoVehiculo(vehiculo.getTipoVehiculo());
            }
            if(vehiculo.getEstado()!= null){
                vehiculoExistente.setEstado(vehiculo.getEstado());
            }
            return vehiculoRepository.save(vehiculoExistente);
        }
        return null;
    }
}
