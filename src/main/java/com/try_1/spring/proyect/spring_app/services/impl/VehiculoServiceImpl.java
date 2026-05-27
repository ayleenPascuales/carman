package com.try_1.spring.proyect.spring_app.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.dto.VehiculoCatalogoDTO;
import com.try_1.spring.proyect.spring_app.models.EstadoVehiculo;
import com.try_1.spring.proyect.spring_app.models.Notificacion;
import com.try_1.spring.proyect.spring_app.models.TipoNotificacion;
import com.try_1.spring.proyect.spring_app.models.Vehiculo;
import com.try_1.spring.proyect.spring_app.repositories.VehiculoRepository;
import com.try_1.spring.proyect.spring_app.services.NotificacionService;
import com.try_1.spring.proyect.spring_app.services.VehiculoService;
import com.try_1.spring.proyect.spring_app.util.VehiculoImagenReferencia;

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
            if(vehiculo.getFoto()!= null){
                vehiculoExistente.setFoto(vehiculo.getFoto());
            }
            return vehiculoRepository.save(vehiculoExistente);
        }
        return null;
    }

    @Override
    public List<VehiculoCatalogoDTO> listarCatalogo() {
        List<VehiculoCatalogoDTO> catalogo = new ArrayList<>();
        for (Vehiculo v : vehiculoRepository.findAll()) {
            if (!esVisibleEnCatalogo(v)) {
                continue;
            }
            catalogo.add(toCatalogoDto(v));
        }
        return catalogo;
    }

    private boolean esVisibleEnCatalogo(Vehiculo v) {
        if (v.getEstado() == null) {
            return true;
        }
        return v.getEstado() == EstadoVehiculo.ACTIVO;
    }

    private VehiculoCatalogoDTO toCatalogoDto(Vehiculo v) {
        VehiculoCatalogoDTO dto = new VehiculoCatalogoDTO();
        dto.setIdVehiculo(v.getIdVehiculo());
        dto.setPlaca(v.getPlaca());
        dto.setMarca(v.getMarca());
        dto.setModelo(v.getModelo());
        dto.setAnio(v.getAnio());
        dto.setCapacidad(v.getCapacidad());
        dto.setTipoVehiculo(v.getTipoVehiculo());
        dto.setEstado(v.getEstado() != null ? v.getEstado().name() : "ACTIVO");
        dto.setNombre(v.getMarca() + " " + v.getModelo());
        dto.setPrecio(calcularPrecioDiario(v));
        dto.setTransmision(inferirTransmision(v));
        dto.setDescripcion(construirDescripcion(v));
        dto.setImagen(VehiculoImagenReferencia.resolver(v.getFoto(), v.getMarca(), v.getModelo()));
        return dto;
    }

    private String calcularPrecioDiario(Vehiculo v) {
        int base = 42000;
        if (v.getAnio() != null) {
            base += Math.max(0, (v.getAnio() - 2018) * 4500);
        }
        String marca = v.getMarca() != null ? v.getMarca().toLowerCase() : "";
        if (marca.contains("bmw") || marca.contains("tesla")) {
            base += 25000;
        }
        if (v.getCapacidad() != null && v.getCapacidad() >= 7) {
            base += 8000;
        }
        return String.format("%,d", base).replace(',', '.');
    }

    private String inferirTransmision(Vehiculo v) {
        String tipo = v.getTipoVehiculo() != null ? v.getTipoVehiculo().toLowerCase() : "";
        if (tipo.contains("eléctric") || tipo.contains("electric")) {
            return "Eléctrico";
        }
        if (tipo.contains("moto")) {
            return "Manual";
        }
        return "Automático";
    }

    private String construirDescripcion(Vehiculo v) {
        return "Vehículo " + v.getMarca() + " " + v.getModelo()
                + (v.getAnio() != null ? " (" + v.getAnio() + ")" : "")
                + ", ideal para tu próximo viaje en Carman.";
    }
}
