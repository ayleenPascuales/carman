package com.try_1.spring.proyect.spring_app.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Imágenes locales (sin depender de CDN externo).
 * Si el vehículo tiene foto subida y el archivo existe, se usa esa ruta.
 */
public final class VehiculoImagenReferencia {

    private static final String BMW = "/img/cars/bmw.jpg";
    private static final String TESLA = "/img/cars/tesla.jpg";
    private static final String TOYOTA_COROLLA = "/img/cars/toyota-corolla.jpg";
    private static final String SUV = "/img/cars/suv.jpg";
    private static final String GENERICO = "/img/cars/generico.jpg";

    private VehiculoImagenReferencia() {
    }

    public static String resolver(String fotoGuardada, String marca, String modelo) {
        if (esRutaAccesible(fotoGuardada)) {
            return fotoGuardada.trim();
        }
        return referenciaPorModelo(marca, modelo);
    }

    public static String referenciaPorModelo(String marca, String modelo) {
        String texto = ((marca != null ? marca : "") + " " + (modelo != null ? modelo : ""))
                .toLowerCase()
                .trim();

        if (texto.contains("tesla")) {
            return TESLA;
        }
        if (texto.contains("bmw")) {
            return BMW;
        }
        if (texto.contains("toyota") && (texto.contains("rav4") || texto.contains("suv"))) {
            return SUV;
        }
        if (texto.contains("toyota") && texto.contains("corolla")) {
            return TOYOTA_COROLLA;
        }
        if (texto.contains("toyota")) {
            return TOYOTA_COROLLA;
        }
        if (texto.contains("mazda") || texto.contains("honda") || texto.contains("kia")) {
            return SUV;
        }
        return GENERICO;
    }

    private static boolean esRutaAccesible(String ruta) {
        if (ruta == null || ruta.isBlank()) {
            return false;
        }
        String limpia = ruta.trim();
        if (limpia.startsWith("http://") || limpia.startsWith("https://")) {
            return true;
        }
        if (!limpia.startsWith("/")) {
            limpia = "/" + limpia;
        }
        String rel = limpia.startsWith("/") ? limpia.substring(1) : limpia;

        Path[] candidatos = {
                Paths.get("src/main/resources/static", rel),
                Paths.get("target/classes/static", rel),
                Paths.get("uploads", rel.replace("uploads/", "")),
                Paths.get(rel.replace("uploads/", "uploads/"))
        };

        for (Path p : candidatos) {
            if (Files.isRegularFile(p)) {
                return true;
            }
        }
        return false;
    }
}
