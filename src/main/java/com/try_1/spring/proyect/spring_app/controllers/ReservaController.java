package com.try_1.spring.proyect.spring_app.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.try_1.spring.proyect.spring_app.dto.CrearReservaRequest;
import com.try_1.spring.proyect.spring_app.dto.HistorialItemDTO;
import com.try_1.spring.proyect.spring_app.dto.ReservaResumenDTO;
import com.try_1.spring.proyect.spring_app.dto.TarifaRutaRequest;
import com.try_1.spring.proyect.spring_app.dto.TarifaRutaResponse;
import com.try_1.spring.proyect.spring_app.models.Reserva;
import com.try_1.spring.proyect.spring_app.services.ReservaService;

@RestController
@RequestMapping("api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public List<Reserva> listar(){
        return reservaService.listar();
    }
    @GetMapping("/{id}")
    public Reserva buscar(@PathVariable Integer id){
        return reservaService.buscarPorId(id);
    }
    @PostMapping
    public Reserva guardar(@RequestBody Reserva reserva){
        return reservaService.guardar(reserva);
    }
    @PutMapping("/{id}")
    public Reserva actualizar(@PathVariable Integer id, @RequestBody Reserva reserva){
        return reservaService.actualizar(id, reserva);
    }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id){
        reservaService.eliminar(id);
    }
    @GetMapping("/fecha")
    public List<Reserva> buscarPorFecha(@RequestParam LocalDate fechaServicio){
        return reservaService.buscarPorFechaServicio(fechaServicio);
    }
    @GetMapping("/cliente/{idCliente}")
    public List<ReservaResumenDTO> buscarPorCliente(@PathVariable Integer idCliente) {
        return reservaService.buscarResumenPorCliente(idCliente);
    }
    @PostMapping("/completa")
    public Reserva crearCompleta(@RequestBody CrearReservaRequest request) {
        return reservaService.crearConRuta(request);
    }

    @PostMapping("/tarifa-ruta")
    public TarifaRutaResponse calcularTarifaRuta(@RequestBody TarifaRutaRequest request) {
        return reservaService.calcularTarifaRuta(request);
    }
    @GetMapping("/cliente/{idCliente}/historial")
    public List<HistorialItemDTO> historialPorCliente(@PathVariable Integer idCliente) {
        return reservaService.obtenerHistorialPorCliente(idCliente);
    }
}


