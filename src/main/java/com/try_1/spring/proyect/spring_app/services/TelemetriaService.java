package com.try_1.spring.proyect.spring_app.services;

import java.util.*;
import com.try_1.spring.proyect.spring_app.models.Telemetria;

public interface TelemetriaService {
    List<Telemetria> listar();
    Telemetria guardar(Telemetria telemetria);
    Telemetria buscarPorId(Integer id);
    void eliminar(Integer id);
}
