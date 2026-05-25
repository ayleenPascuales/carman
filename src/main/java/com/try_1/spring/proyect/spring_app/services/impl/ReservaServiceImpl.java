package com.try_1.spring.proyect.spring_app.services.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.models.Notificacion;
import com.try_1.spring.proyect.spring_app.models.Reserva;
import com.try_1.spring.proyect.spring_app.models.Ruta;
import com.try_1.spring.proyect.spring_app.models.TipoNotificacion;
import com.try_1.spring.proyect.spring_app.repositories.ReservaRepository;
import com.try_1.spring.proyect.spring_app.services.MapServices;
import com.try_1.spring.proyect.spring_app.services.NotificacionService;
import com.try_1.spring.proyect.spring_app.services.ReservaService;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private MapServices mapServices;
    @Autowired
    private NotificacionService notificacionService;


    @Override
    public List<Reserva> listar(){
        return reservaRepository.findAll();
    }
    @Override
    public Reserva guardar(Reserva reserva){

        if(reserva.getRuta()!= null){
            
            Ruta ruta = reserva.getRuta();
            if(ruta.getOrigenLon()!= null && ruta.getOrigenLat()!= null &&
                ruta.getDestinoLon()!= null && ruta.getDestinoLat()!= null){
                    
                    Ruta datosRuta = mapServices.calcularRuta(ruta.getOrigenLon(),ruta.getOrigenLat(),
                            ruta.getDestinoLon(),ruta.getDestinoLat());
            
            if(datosRuta != null){
                ruta.setDistancia(datosRuta.getDistancia());
                ruta.setDuracion(datosRuta.getDuracion());
            }
            }
        }
        Reserva reservaGuardada = reservaRepository.save(reserva);
        
        Notificacion n =new Notificacion();
        n.setReserva(reservaGuardada);
        n.setTipo(TipoNotificacion.RESERVA);
        n.setMensaje("Nueva reserva creada para el "+ reservaGuardada.getFechaServicio());
        notificacionService.guardar(n);
        
        return reservaGuardada;
    }
    @Override
    public Reserva buscarPorId(Integer id){
        return reservaRepository.findById(id).orElse(null);
    }
    @Override
    public void eliminar(Integer id){
        reservaRepository.deleteById(id);
    }
    @Override
    public List<Reserva> buscarPorFechaServicio(LocalDate fechaServicio) {
    return reservaRepository.findByFechaServicio(fechaServicio);
    }
    @Override
    public Reserva actualizar(Integer id, Reserva reserva){

        Reserva reservaExistente = reservaRepository.findById(id).orElse(null);

        if(reservaExistente != null){

                if(reserva.getFechaServicio()!= null){
                    reservaExistente.setFechaServicio(reserva.getFechaServicio());
                }
                if(reserva.getHoraEntrega()!= null){
                    reservaExistente.setHoraEntrega(reserva.getHoraEntrega());
                }
                if(reserva.getRuta()!= null && reservaExistente.getRuta()!= null){
                    Ruta rutaActual = reservaExistente.getRuta();
                    Ruta nuevaRuta = reserva.getRuta();

                    if(nuevaRuta.getOrigen()!= null){
                        rutaActual.setOrigen(nuevaRuta.getOrigen());
                    }
                    if(nuevaRuta.getDestino()!= null){
                        rutaActual.setDestino(nuevaRuta.getDestino());
                    }
                    if(nuevaRuta.getOrigenLon()!= null){
                        rutaActual.setOrigenLon(nuevaRuta.getOrigenLon());
                    }

                    if(nuevaRuta.getOrigenLat()!= null){
                        rutaActual.setOrigenLat(nuevaRuta.getOrigenLat());
                    }

                    if(nuevaRuta.getDestinoLon()!= null){
                        rutaActual.setDestinoLon(nuevaRuta.getDestinoLon());
                    }

                    if(nuevaRuta.getDestinoLat()!= null){
                        rutaActual.setDestinoLat(nuevaRuta.getDestinoLat());
                    }
                    if(rutaActual.getOrigenLon()!= null && rutaActual.getOrigenLat()!= null &&
                        rutaActual.getDestinoLon()!= null && rutaActual.getDestinoLat()!= null){
                    
                    Ruta datosRuta = mapServices.calcularRuta(rutaActual.getOrigenLon(),rutaActual.getOrigenLat(),
                            rutaActual.getDestinoLon(),rutaActual.getDestinoLat());
            
            if(datosRuta != null){
                rutaActual.setDistancia(datosRuta.getDistancia());
                rutaActual.setDuracion(datosRuta.getDuracion());
            }
            }
        }
        return reservaRepository.save(reservaExistente);
            }
            return null;
    }   
}

