package com.try_1.spring.proyect.spring_app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class EjemploController {
    
    @GetMapping("/Home")
    
    public String info(){
        
        return "Home";
    }
}
