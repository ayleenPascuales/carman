package com.try_1.spring.proyect.spring_app.services.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.models.EstadoOrdenPago;
import com.try_1.spring.proyect.spring_app.models.OrdenPago;
import com.try_1.spring.proyect.spring_app.models.Reserva;
import com.try_1.spring.proyect.spring_app.repositories.OrdenPagoRepository;
import com.try_1.spring.proyect.spring_app.repositories.ReservaRepository;
import com.try_1.spring.proyect.spring_app.services.OrdenPagoService;
import com.try_1.spring.proyect.spring_app.services.PdfService;

@Service
public class OrdenPagoServiceImpl implements OrdenPagoService {

    @Autowired
    private OrdenPagoRepository ordenPagoRepository;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private ReservaRepository reservaRepository;

    @Override
    public List<OrdenPago> listar(){
        return ordenPagoRepository.findAll();
    }
    @Override
    public OrdenPago guardar(OrdenPago ordenPago){
        return ordenPagoRepository.save(ordenPago);
    }
    @Override
    public OrdenPago buscarPorId(Integer id){
        return ordenPagoRepository.findById(id).orElse(null);
    }
    @Override
    public void eliminar(Integer id){
        ordenPagoRepository.deleteById(id);
    }
    @Override
    public List<OrdenPago> buscarPorFechaEmision(LocalDate fechaEmision){
        return ordenPagoRepository.findByFechaEmision(fechaEmision);
    }

    @Override
    public byte[] generarPdf(Integer idOrdenPago) {

        OrdenPago orden = ordenPagoRepository.findById(idOrdenPago).orElse(null);

        if (orden != null) {
        byte[] pdf = pdfService.generarOrdenPagoPdf(orden);
        orden.setRutaPdf("generado");
        ordenPagoRepository.save(orden);
        return pdf;
        
        }
    return null;
    }

    @Override
    public OrdenPago generarOrdenDesdeReserva(Integer idReserva) {

        Reserva reserva = reservaRepository.findById(idReserva).orElse(null);

        if (reserva == null) return null;

        OrdenPago orden = new OrdenPago();

        orden.setReserva(reserva);
        orden.setFechaEmision(LocalDate.now());
        orden.setTotal(reserva.getPrecioEstimado());
        orden.setDetalles("Orden generada desde reserva");
        orden.setEstado(EstadoOrdenPago.ACTIVA);

        return ordenPagoRepository.save(orden);
    }

    //  AUTOMATICA CUANDO CAMBIA RESERVA
    public OrdenPago regenerarOrdenPorCambioReserva(Reserva reserva) {

        // 1. buscar órdenes anteriores
        List<OrdenPago> anteriores = ordenPagoRepository.findByReserva(reserva);

        for (OrdenPago op : anteriores) {
            op.setEstado(EstadoOrdenPago.ANULADA);
            ordenPagoRepository.save(op);
        }
        OrdenPago nueva = new OrdenPago();

        nueva.setReserva(reserva);
        nueva.setFechaEmision(LocalDate.now());
        nueva.setTotal(reserva.getPrecioEstimado());
        nueva.setDetalles("Orden generada automáticamente por cambio de reserva");
        nueva.setEstado(EstadoOrdenPago.ACTIVA);

        return ordenPagoRepository.save(nueva);
    }

}
