package com.try_1.spring.proyect.spring_app.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class RegistroExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSize(MaxUploadSizeExceededException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error",
                "Las imágenes superan el tamaño permitido (máx. 15 MB por archivo). "
                        + "Comprime las fotos o usa archivos más pequeños e intenta de nuevo.");
        return "redirect:/registro2";
    }
}
