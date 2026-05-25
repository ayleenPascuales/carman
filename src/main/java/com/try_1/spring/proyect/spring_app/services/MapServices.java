package com.try_1.spring.proyect.spring_app.services;
import java.math.BigDecimal;

import com.try_1.spring.proyect.spring_app.models.Ruta;

public interface MapServices {
    
    Ruta calcularRuta(BigDecimal  origenLon, BigDecimal  origenLat, BigDecimal  destinoLon, BigDecimal  destinoLat);
}
