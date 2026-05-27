package com.try_1.spring.proyect.spring_app.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.dto.CrearReservaRequest;
import com.try_1.spring.proyect.spring_app.dto.HistorialItemDTO;
import com.try_1.spring.proyect.spring_app.dto.ReservaResumenDTO;
import com.try_1.spring.proyect.spring_app.dto.TarifaRutaRequest;
import com.try_1.spring.proyect.spring_app.dto.TarifaRutaResponse;
import com.try_1.spring.proyect.spring_app.models.Cliente;
import com.try_1.spring.proyect.spring_app.models.Conductor;
import com.try_1.spring.proyect.spring_app.models.ContratoAlquiler;
import com.try_1.spring.proyect.spring_app.models.EstadoReserva;
import com.try_1.spring.proyect.spring_app.models.Notificacion;
import com.try_1.spring.proyect.spring_app.models.OrdenPago;
import com.try_1.spring.proyect.spring_app.models.Pago;
import com.try_1.spring.proyect.spring_app.models.Reserva;
import com.try_1.spring.proyect.spring_app.models.Ruta;
import com.try_1.spring.proyect.spring_app.models.TipoNotificacion;
import com.try_1.spring.proyect.spring_app.models.Vehiculo;
import com.try_1.spring.proyect.spring_app.repositories.ClienteRepository;
import com.try_1.spring.proyect.spring_app.repositories.ConductorRepository;
import com.try_1.spring.proyect.spring_app.repositories.ContratoAlquilerRepository;
import com.try_1.spring.proyect.spring_app.repositories.OrdenPagoRepository;
import com.try_1.spring.proyect.spring_app.repositories.PagoRepository;
import com.try_1.spring.proyect.spring_app.repositories.ReservaRepository;
import com.try_1.spring.proyect.spring_app.repositories.RutaRepository;
import com.try_1.spring.proyect.spring_app.repositories.VehiculoRepository;
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
    @Autowired
    private RutaRepository rutaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private VehiculoRepository vehiculoRepository;
    @Autowired
    private ConductorRepository conductorRepository;
    @Autowired
    private OrdenPagoRepository ordenPagoRepository;
    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private ContratoAlquilerRepository contratoAlquilerRepository;


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

    @Override
    public List<ReservaResumenDTO> buscarResumenPorCliente(Integer idCliente) {
        return reservaRepository.findByCliente_IdCliente(idCliente).stream()
            .map(this::toResumenDTO)
            .collect(Collectors.toList());
    }

    @Override
    public Reserva crearConRuta(CrearReservaRequest request) {
        Cliente cliente = clienteRepository.findById(request.getIdCliente()).orElse(null);
        Vehiculo vehiculo = vehiculoRepository.findById(request.getIdVehiculo()).orElse(null);

        if (cliente == null || vehiculo == null) {
            return null;
        }

        Ruta ruta = new Ruta();
        ruta.setOrigen(request.getOrigen());
        ruta.setDestino(request.getDestino());
        ruta.setOrigenLon(request.getOrigenLon());
        ruta.setOrigenLat(request.getOrigenLat());
        ruta.setDestinoLon(request.getDestinoLon());
        ruta.setDestinoLat(request.getDestinoLat());
        ruta.setFecha(request.getFechaServicio());
        ruta = rutaRepository.save(ruta);

        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setVehiculo(vehiculo);
        reserva.setRuta(ruta);
        reserva.setFechaServicio(request.getFechaServicio());
        reserva.setHoraEntrega(request.getHoraEntrega());
        reserva.setFechaReserva(LocalDate.now());
        // Precio estimado se calcula a partir de distancia real (cuando hay coordenadas).
        TarifaRutaResponse tarifa = calcularTarifaRuta(buildTarifaRequest(request));
        if (tarifa != null && tarifa.getPrecioEstimado() != null) {
            reserva.setPrecioEstimado(tarifa.getPrecioEstimado());
        } else {
            reserva.setPrecioEstimado(request.getPrecioEstimado());
        }
        reserva.setEstado(EstadoReserva.Pendiente);

        if (Boolean.TRUE.equals(request.getConConductor()) && request.getIdConductor() != null) {
            Conductor conductor = conductorRepository.findById(request.getIdConductor()).orElse(null);
            reserva.setConductor(conductor);
        }

        return guardar(reserva);
    }

    @Override
    public TarifaRutaResponse calcularTarifaRuta(TarifaRutaRequest request) {
        if (request == null ||
            request.getOrigenLon() == null || request.getOrigenLat() == null ||
            request.getDestinoLon() == null || request.getDestinoLat() == null) {
            return null;
        }

        Ruta datosRuta = mapServices.calcularRuta(
                request.getOrigenLon(),
                request.getOrigenLat(),
                request.getDestinoLon(),
                request.getDestinoLat()
        );

        if (datosRuta == null || datosRuta.getDistancia() == null) {
            return null;
        }

        int dias = request.getDias() != null ? request.getDias() : 1;
        boolean conConductor = Boolean.TRUE.equals(request.getConConductor());

        BigDecimal baseDiaria = conConductor ? new BigDecimal("110000") : new BigDecimal("75000");
        BigDecimal tarifaKm = conConductor ? new BigDecimal("2000") : new BigDecimal("1500");

        BigDecimal precio = baseDiaria.multiply(BigDecimal.valueOf(dias))
                .add(datosRuta.getDistancia().multiply(tarifaKm));

        // Descuento 10% para 7+ días (misma lógica UX anterior).
        if (dias >= 7) {
            precio = precio.multiply(new BigDecimal("0.90"));
        }

        TarifaRutaResponse resp = new TarifaRutaResponse();
        resp.setPrecioEstimado(precio);
        resp.setDistanciaKm(datosRuta.getDistancia());
        resp.setDuracionMin(datosRuta.getDuracion());
        return resp;
    }

    private TarifaRutaRequest buildTarifaRequest(CrearReservaRequest request) {
        TarifaRutaRequest t = new TarifaRutaRequest();
        t.setOrigenLon(request.getOrigenLon());
        t.setOrigenLat(request.getOrigenLat());
        t.setDestinoLon(request.getDestinoLon());
        t.setDestinoLat(request.getDestinoLat());
        t.setConConductor(request.getConConductor());
        t.setDias(request.getDias());
        return t;
    }

    @Override
    public List<HistorialItemDTO> obtenerHistorialPorCliente(Integer idCliente) {
        List<HistorialItemDTO> historial = new ArrayList<>();

        for (Reserva reserva : reservaRepository.findByCliente_IdCliente(idCliente)) {
            HistorialItemDTO item = new HistorialItemDTO();
            item.setTipo("RESERVA");
            item.setIdReferencia(reserva.getIdReserva());
            item.setIdReserva(reserva.getIdReserva());
            item.setTitulo("Reserva #" + reserva.getIdReserva());
            item.setDetalle(describirVehiculo(reserva) + " · " + describirRuta(reserva));
            item.setFecha(reserva.getFechaServicio() != null ? reserva.getFechaServicio().toString() : "");
            item.setEstado(reserva.getEstado() != null ? reserva.getEstado().name() : "");
            item.setMonto(reserva.getPrecioEstimado() != null ? reserva.getPrecioEstimado().toPlainString() : null);
            historial.add(item);
        }

        for (OrdenPago orden : ordenPagoRepository.findByReserva_Cliente_IdCliente(idCliente)) {
            HistorialItemDTO item = new HistorialItemDTO();
            item.setTipo("ORDEN_PAGO");
            item.setIdReferencia(orden.getIdOrdenPago());
            item.setIdReserva(orden.getReserva() != null ? orden.getReserva().getIdReserva() : null);
            item.setTitulo("Orden de pago #" + orden.getIdOrdenPago());
            item.setDetalle(orden.getDetalles());
            item.setFecha(orden.getFechaEmision() != null ? orden.getFechaEmision().toString() : "");
            item.setEstado(orden.getEstado() != null ? orden.getEstado().name() : "");
            item.setMonto(orden.getTotal() != null ? orden.getTotal().toPlainString() : null);
            historial.add(item);
        }

        for (Pago pago : pagoRepository.findByReserva_Cliente_IdCliente(idCliente)) {
            HistorialItemDTO item = new HistorialItemDTO();
            item.setTipo("PAGO");
            item.setIdReferencia(pago.getIdPago());
            item.setIdReserva(pago.getReserva() != null ? pago.getReserva().getIdReserva() : null);
            item.setTitulo("Pago #" + pago.getIdPago());
            item.setDetalle(pago.getMetodo() != null ? pago.getMetodo().getDescripcion() : "Pago registrado");
            item.setFecha(pago.getFechaHoraPago() != null ? pago.getFechaHoraPago().toString() : "");
            item.setEstado(pago.getEstado() != null ? pago.getEstado().name() : "");
            item.setMonto(pago.getMonto() != null ? pago.getMonto().toPlainString() : null);
            historial.add(item);
        }

        for (ContratoAlquiler contrato : contratoAlquilerRepository.findByCliente_IdCliente(idCliente)) {
            HistorialItemDTO item = new HistorialItemDTO();
            item.setTipo("CONTRATO");
            item.setIdReferencia(contrato.getIdContratoAlquiler());
            item.setIdReserva(contrato.getReserva() != null ? contrato.getReserva().getIdReserva() : null);
            item.setTitulo("Contrato #" + contrato.getIdContratoAlquiler());
            item.setDetalle(contrato.getCondiciones());
            item.setFecha(contrato.getFechaGeneracion() != null ? contrato.getFechaGeneracion().toString() : "");
            item.setEstado(contrato.getEstado() != null ? contrato.getEstado().name() : "");
            historial.add(item);
        }

        historial.sort(Comparator.comparing(HistorialItemDTO::getFecha, Comparator.nullsLast(Comparator.reverseOrder())));
        return historial;
    }

    private ReservaResumenDTO toResumenDTO(Reserva reserva) {
        ReservaResumenDTO dto = new ReservaResumenDTO();
        dto.setIdReserva(reserva.getIdReserva());
        dto.setVehiculo(describirVehiculo(reserva));
        dto.setOrigen(reserva.getRuta() != null ? reserva.getRuta().getOrigen() : null);
        dto.setDestino(reserva.getRuta() != null ? reserva.getRuta().getDestino() : null);
        dto.setFechaServicio(reserva.getFechaServicio());
        dto.setHoraEntrega(reserva.getHoraEntrega());
        dto.setFechaReserva(reserva.getFechaReserva());
        dto.setPrecioEstimado(reserva.getPrecioEstimado());
        dto.setEstado(reserva.getEstado() != null ? reserva.getEstado().name() : null);
        return dto;
    }

    private String describirVehiculo(Reserva reserva) {
        if (reserva.getVehiculo() == null) {
            return "Vehículo no asignado";
        }
        return reserva.getVehiculo().getMarca() + " " + reserva.getVehiculo().getModelo();
    }

    private String describirRuta(Reserva reserva) {
        if (reserva.getRuta() == null) {
            return "Sin ruta";
        }
        return reserva.getRuta().getOrigen() + " → " + reserva.getRuta().getDestino();
    }
}

