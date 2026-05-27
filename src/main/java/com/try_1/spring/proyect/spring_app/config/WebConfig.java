package com.try_1.spring.proyect.spring_app.config;

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String currentDir = Paths.get("").toAbsolutePath().toString();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(
                        "file:" + currentDir + "/uploads/",
                        "file:" + currentDir + "/src/main/resources/static/uploads/",
                        "classpath:/static/uploads/");
    }
}
