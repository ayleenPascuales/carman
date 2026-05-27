package com.try_1.spring.proyect.spring_app.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.try_1.spring.proyect.spring_app.dto.RegistrarPagoRequest;
import com.try_1.spring.proyect.spring_app.models.OrdenPago;
import com.try_1.spring.proyect.spring_app.models.Pago;
import com.try_1.spring.proyect.spring_app.services.PagoService;




@RestController
@RequestMapping("api/pagos")
public class PagoController {
    @Autowired
    private PagoService pagoService;

    @GetMapping
    public List<Pago> listar() {
        return pagoService.listar();
    }

    @GetMapping("/{id}")
    public Pago buscar(@PathVariable Integer id) {
        return pagoService.buscarPorId(id);
    }

    @PostMapping
    public Pago guardar(@RequestBody Pago pago) {
        return pagoService.guardar(pago);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        pagoService.eliminar(id);
    }

    @PostMapping("/procesar/{id}")
    public Pago procesar(@PathVariable Integer id) {
        return pagoService.procesarPago(id);
    }

    @PostMapping("/desde-orden")
    public Pago crearPagoDesdeOrden(@RequestBody OrdenPago orden){
        return pagoService.crearPagoDesdeOrden(orden);
    }

    @GetMapping("/fecha")
    public List<Pago> buscarPorFechaHoraPago(@RequestParam LocalDateTime fechaHoraPago){
        return pagoService.buscarPorFechaHoraPago(fechaHoraPago);
    }
    @GetMapping("/cliente/{idCliente}")
    public List<Pago> buscarPorCliente(@PathVariable Integer idCliente) {
        return pagoService.buscarPorCliente(idCliente);
    }
    @GetMapping("/reserva/{idReserva}")
    public List<Pago> buscarPorReserva(@PathVariable Integer idReserva) {
        return pagoService.buscarPorReserva(idReserva);
    }
    @PostMapping("/registrar")
    public Pago registrarPago(@RequestBody RegistrarPagoRequest request) {
        return pagoService.registrarPagoCompleto(request);
    }
}
