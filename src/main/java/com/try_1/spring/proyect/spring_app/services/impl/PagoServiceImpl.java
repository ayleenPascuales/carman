package com.try_1.spring.proyect.spring_app.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.models.EstadoOrdenPago;
import com.try_1.spring.proyect.spring_app.models.EstadoPago;
import com.try_1.spring.proyect.spring_app.models.Notificacion;
import com.try_1.spring.proyect.spring_app.models.OrdenPago;
import com.try_1.spring.proyect.spring_app.models.Pago;
import com.try_1.spring.proyect.spring_app.models.TipoNotificacion;
import com.try_1.spring.proyect.spring_app.repositories.OrdenPagoRepository;
import com.try_1.spring.proyect.spring_app.repositories.PagoRepository;
import com.try_1.spring.proyect.spring_app.services.NotificacionService;
import com.try_1.spring.proyect.spring_app.services.PagoService;
import com.try_1.spring.proyect.spring_app.services.PasarelaPagoService;

@Service
public class PagoServiceImpl implements PagoService{

    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private PasarelaPagoService pasarelaPagoService;
    @Autowired
    private OrdenPagoRepository ordenPagoRepository;
    @Autowired
    private NotificacionService notificacionService;

    @Override
    public List<Pago> listar(){
        return pagoRepository.findAll();
    }
    @Override
    public Pago guardar(Pago pago){
        return pagoRepository.save(pago);
    }
    @Override
    public Pago buscarPorId(Integer id){
        return pagoRepository.findById(id).orElse(null);
    }
    @Override
    public void eliminar(Integer id){
        pagoRepository.deleteById(id);
    }
    @Override
    public List<Pago> buscarPorFechaHoraPago(LocalDateTime fechaHoraPago){
        return pagoRepository.findByFechaHoraPago(fechaHoraPago);
    }
    @Override
    public Pago crearPagoDesdeOrden(OrdenPago orden) {

    Pago pago = new Pago();

    pago.setOrdenPago(orden);

    pago.setFechaHoraPago(LocalDateTime.now());

    pago.setEstado(EstadoPago.PENDIENTE);

    return pagoRepository.save(pago);
    }
    @Override
    public Pago procesarPago(Integer idPago){

        Pago pago = pagoRepository.findById(idPago).orElse(null);

        if(pago == null){
            return null;
        }   
        pago.setEstado(EstadoPago.PENDIENTE);

        pagoRepository.save(pago);

        boolean aprobado = pasarelaPagoService.procesarPago(pago);

        OrdenPago orden = pago.getOrdenPago();

        if(aprobado){

            pago.setEstado(EstadoPago.PAGADO);
            orden.setEstado(EstadoOrdenPago.PAGADA);
            ordenPagoRepository.save(orden);
            System.out.println("Pago aprobado correctamente");
            
            Notificacion n = new Notificacion();
            n.setPago(pago);
            n.setTipo(TipoNotificacion.PAGO);
            n.setMensaje("Pago aprobado para la orden #"+ orden.getIdOrdenPago());
            notificacionService.guardar(n);

        }else{
        pago.setEstado(EstadoPago.RECHAZADO);
        System.out.println( "Pago rechazado: fondos insuficientes");
        
        Notificacion n = new Notificacion();
        n.setPago(pago);
        n.setTipo(TipoNotificacion.PAGO);
        n.setMensaje("Pago rechazado para la orden #"+ orden.getIdOrdenPago());
        notificacionService.guardar(n);
        }
        return pagoRepository.save(pago);
    }
}
