package com.try_1.spring.proyect.spring_app.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.try_1.spring.proyect.spring_app.models.OrdenPago;
import com.try_1.spring.proyect.spring_app.services.OrdenPagoService;

@RestController
@RequestMapping("api/ordenPago")
public class OrdenPagoController {

    @Autowired
    private OrdenPagoService ordenPagoService;

    @GetMapping
    public List<OrdenPago> listar(){
        return ordenPagoService.listar();
    }
    @GetMapping("/{id}")
    public OrdenPago BuscarPorId(@PathVariable Integer id){
        return ordenPagoService.buscarPorId(id);
    }
    @PostMapping
    public OrdenPago guardar(@RequestBody OrdenPago ordenPago){
        return ordenPagoService.guardar(ordenPago);
    }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id){
        ordenPagoService.eliminar(id);
    }
    @GetMapping("/fechaEmision")
    public List <OrdenPago> buscarPorFechaEmision(@RequestParam LocalDate fechaEmision){
        return ordenPagoService.buscarPorFechaEmision(fechaEmision);
    }
    @GetMapping("/{id}/ordenPago.pdf")
    public ResponseEntity<byte[]> generarPdf(@PathVariable Integer id) {

        byte[] pdf = ordenPagoService.generarPdf(id);

        if (pdf == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().header("Content-Type", "application/pdf")
            .header("Content-Disposition","attachment; filename=OrdenDePago_" + id + "_" + java.time.LocalDate.now() + ".pdf").body(pdf);
    }
    @PostMapping("/desdeReserva/{idReserva}")
    public OrdenPago generarDesdeReserva(@PathVariable Integer idReserva) {
        return ordenPagoService.generarOrdenDesdeReserva(idReserva);
    }


}
