package com.try_1.spring.proyect.spring_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.try_1.spring.proyect.spring_app.models.Vehiculo;


public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer>{
    
    Vehiculo findByPlaca(String Placa);
    Vehiculo findByMarca(String Marca);
    Vehiculo findByModelo(String Modelo);

    void deleteByPlaca(String Placa);
}
