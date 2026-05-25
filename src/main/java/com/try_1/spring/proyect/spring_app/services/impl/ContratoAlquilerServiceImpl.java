package com.try_1.spring.proyect.spring_app.services.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.models.ContratoAlquiler;
import com.try_1.spring.proyect.spring_app.models.EstadoContrato;
import com.try_1.spring.proyect.spring_app.models.Notificacion;
import com.try_1.spring.proyect.spring_app.models.TipoNotificacion;
import com.try_1.spring.proyect.spring_app.repositories.ContratoAlquilerRepository;
import com.try_1.spring.proyect.spring_app.services.ContratoAlquilerService;
import com.try_1.spring.proyect.spring_app.services.NotificacionService;
import com.try_1.spring.proyect.spring_app.services.PdfService;

@Service
public class ContratoAlquilerServiceImpl implements ContratoAlquilerService {

    @Autowired
    private ContratoAlquilerRepository contratoAlquilerRepository;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private NotificacionService notificacionService;


    @Override
    public List<ContratoAlquiler> listar(){
        return contratoAlquilerRepository.findAll();
    }
    @Override
    public ContratoAlquiler guardar(ContratoAlquiler contratoAlquiler){
        contratoAlquiler.setFechaGeneracion( LocalDate.now());
        if(contratoAlquiler.getEstado() == null){
            contratoAlquiler.setEstado(EstadoContrato.PENDIENTE_FIRMA);
        }
        
        ContratoAlquiler contratoGuardado = contratoAlquilerRepository.save(contratoAlquiler);

        Notificacion n =new Notificacion();
        n.setTipo(TipoNotificacion.CONTRATO);
        n.setMensaje("Contrato generado para la reserva #"+ contratoGuardado.getReserva().getIdReserva());  
        notificacionService.guardar(n);

        return contratoGuardado;
    }
    @Override
    public ContratoAlquiler buscarPorId(Integer id){
        return contratoAlquilerRepository.findById(id).orElse(null);
    }
    @Override
    public void eliminar(Integer id){
        contratoAlquilerRepository.deleteById(id);
    }
    @Override
    public ContratoAlquiler actualizar(Integer id, ContratoAlquiler contratoAlquiler){

        ContratoAlquiler contratoActual = contratoAlquilerRepository.findById(id).orElse(null);

            if(contratoActual != null){

            if(contratoAlquiler.getFechaInicio() != null){
            contratoActual.setFechaInicio(contratoAlquiler.getFechaInicio());
            }

            if(contratoAlquiler.getFechaFin() != null){
            contratoActual.setFechaFin(contratoAlquiler.getFechaFin());
            }

            if(contratoAlquiler.getCondiciones() != null){
            contratoActual.setCondiciones(contratoAlquiler.getCondiciones());
            }

            contratoActual.setFechaGeneracion(LocalDate.now());

            return contratoAlquilerRepository.save(contratoActual);
        }
        return null;
    }
    @Override
   public void activarContrato(Integer id){
        ContratoAlquiler contrato = contratoAlquilerRepository.findById(id).orElse(null);

        if(contrato != null){
            contrato.setEstado(EstadoContrato.ACTIVO);
            contratoAlquilerRepository.save(contrato);
        }
    }
    @Override
    public void finalizarContrato(Integer id){
        ContratoAlquiler contrato = contratoAlquilerRepository.findById(id).orElse(null);

        if(contrato != null){
            contrato.setEstado(EstadoContrato.FINALIZADO);
            contratoAlquilerRepository.save(contrato);
        }
    }
    @Override
     public byte[] generarPdf(Integer idContrato) {

        ContratoAlquiler contrato = contratoAlquilerRepository.findById(idContrato).orElse(null);

        if (contrato != null) {

            byte[] pdf = pdfService.generarContratoPdf(contrato); 

            contrato.setArchivoPdf("generado"); 

            if (contrato.getEstado() == EstadoContrato.PENDIENTE_FIRMA) {
            contrato.setEstado(EstadoContrato.ACTIVO);
        }
            contratoAlquilerRepository.save(contrato);

            return pdf;
        }

        return null;
    }

}
