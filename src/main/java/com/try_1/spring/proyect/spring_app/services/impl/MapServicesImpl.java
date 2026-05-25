package com.try_1.spring.proyect.spring_app.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.try_1.spring.proyect.spring_app.models.Ruta;
import com.try_1.spring.proyect.spring_app.services.MapServices;

@Service
public class MapServicesImpl implements MapServices {
    
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ors.api.key}")
    private String API_KEY;

    @Override
    public Ruta calcularRuta(BigDecimal  origenLon,BigDecimal  origenLat,BigDecimal  destinoLon,BigDecimal  destinoLat){

        try {
        String url = "https://api.openrouteservice.org/v2/directions/driving-car";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", API_KEY);

         String body =
        """
        {
          "coordinates":[
            [%f,%f],
            [%f,%f]
          ]
        }
        """.formatted(origenLon.doubleValue(),origenLat.doubleValue(),destinoLon.doubleValue(),destinoLat.doubleValue());

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response =restTemplate.exchange(url,HttpMethod.POST,entity, Map.class);

        Map<String, Object> data = response.getBody();
        if(data == null){
            return null;
        }

        List features =(List) data.get("features");
        if(features == null ||features.isEmpty()){
                return null;
            }

        Map feature =(Map) features.get(0);

        Map properties =(Map) feature.get("properties");

        Map summary =(Map) properties.get("summary");

        Double distanciaDouble =((Number) summary.get("distance")).doubleValue();
        BigDecimal distancia = BigDecimal.valueOf( distanciaDouble / 1000);

        Double duracionDouble =((Number) summary.get("duration")).doubleValue();
        Integer duracion = (int) (duracionDouble / 60);

    return new Ruta(distancia,duracion);

    } catch (Exception e){

            System.out.println( "Error al calcular ruta: "+ e.getMessage());
            return null;
        }
    } 
}   
