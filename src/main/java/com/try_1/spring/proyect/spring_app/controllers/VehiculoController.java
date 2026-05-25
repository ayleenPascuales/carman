package com.try_1.spring.proyect.spring_app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.try_1.spring.proyect.spring_app.models.Vehiculo;
import com.try_1.spring.proyect.spring_app.services.VehiculoService;

@RestController
@RequestMapping("api/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @GetMapping
    public List<Vehiculo> listar(){
        return vehiculoService.listar();
    }
    @GetMapping("/{id}")
    public Vehiculo buscarPorId(@PathVariable Integer id){
        return vehiculoService.buscarPorId(id);
    }
    @GetMapping("/placa/{placa}")
    public Vehiculo buscarPorPlaca(@PathVariable String placa){
        return vehiculoService.buscarPorPlaca(placa);
    }
    @GetMapping("/marca/{marca}")
    public Vehiculo buscarPorMarca(@PathVariable String marca){
        return vehiculoService.buscarPorMarca(marca);
    }
    @GetMapping("/modelo/{modelo}")
    public Vehiculo buscarPorModelo(@PathVariable String modelo){
        return vehiculoService.buscarPorModelo(modelo);
    }
    @PostMapping
    public Vehiculo guardar(@RequestBody Vehiculo vehiculo){
        return vehiculoService.guardar(vehiculo);
    }
    @PutMapping("/placa/{placa}")
    public Vehiculo actualizar(@PathVariable String placa, @RequestBody Vehiculo vehiculo){
        return vehiculoService.actualizar(placa, vehiculo);
    }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id){
        vehiculoService.eliminar(id);
    }
    @DeleteMapping("/placa/{placa}")
    public void eliminarPorPlaca(@PathVariable String placa){
        vehiculoService.eliminarPorPlaca(placa);
    }

}
