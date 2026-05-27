package com.try_1.spring.proyect.spring_app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.try_1.spring.proyect.spring_app.models.ContratoAlquiler;
import com.try_1.spring.proyect.spring_app.services.ContratoAlquilerService;

@RestController
@RequestMapping("api/contratoAlquiler")
public class ContratoAlquilerController {

    @Autowired
    private ContratoAlquilerService contratoAlquilerService;

    @GetMapping
    public List<ContratoAlquiler> listar(){
        return contratoAlquilerService.listar();
    }
    @GetMapping("/{id}")
    public ContratoAlquiler buscarPorId(@PathVariable Integer id){
        return contratoAlquilerService.buscarPorId(id);
    }
    @PostMapping
    public ContratoAlquiler guardar(@RequestBody ContratoAlquiler contratoAlquiler){
        return contratoAlquilerService.guardar(contratoAlquiler);
    }
    @PutMapping("/{id}")
    public ContratoAlquiler actualizar(@PathVariable Integer id, @RequestBody ContratoAlquiler contratoAlquiler){
        return contratoAlquilerService.actualizar(id, contratoAlquiler);
    }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id){
        contratoAlquilerService.eliminar(id);
    }
    @PatchMapping("/{id}/activar")
    public void activar(@PathVariable Integer id){
        contratoAlquilerService.activarContrato(id);
    }
    @PatchMapping("/{id}/finalizar")
    public void finalizar(@PathVariable Integer id) {    
        contratoAlquilerService.finalizarContrato(id);
    }
    @GetMapping("/{id}/contrato.pdf")
    public ResponseEntity<byte[]> generarPdf(@PathVariable Integer id) {

        byte[] pdf = contratoAlquilerService.generarPdf(id);

        if (pdf == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("contrato_" + id + ".pdf").build());

    return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @GetMapping("/cliente/{idCliente}")
    public List<ContratoAlquiler> buscarPorCliente(@PathVariable Integer idCliente) {
        return contratoAlquilerService.buscarPorCliente(idCliente);
    }

    @GetMapping("/reserva/{idReserva}")
    public ContratoAlquiler buscarPorReserva(@PathVariable Integer idReserva) {
        return contratoAlquilerService.buscarPorReserva(idReserva);
    }

    @PostMapping("/desdeReserva/{idReserva}")
    public ContratoAlquiler generarDesdeReserva(@PathVariable Integer idReserva) {
        return contratoAlquilerService.generarDesdeReserva(idReserva);
    }
    
}
