package com.try_1.spring.proyect.spring_app.services.impl;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.models.Pago;
import com.try_1.spring.proyect.spring_app.services.PasarelaPagoService;

@Service
public class PasarelaPagoServiceImpl implements PasarelaPagoService{

    private final Random random = new Random();

    @Override
    public boolean procesarPago(Pago pago){

        try {
            System.out.println("Conectando con pasarela de pago...");
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Procesando pago con método: "+ pago.getMetodo().getTipo());
        System.out.println("Monto: $" + pago.getMonto());

        int probabilidad = random.nextInt(100);

        if(probabilidad < 70){
            System.out.println("Pago aprobado correctamente");
            return true;
        }else if(probabilidad < 85){
            System.out.println("Pago rechazado: fondos insuficientes");
        }else{
            System.out.println("Pago rechazado: error bancario");
        }

        return false;
    }
}
