package com.bandhuram.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/shop/**")
                .addResourceLocations("file:" + uploadDir + "/");

        // WebMvcConfig.java — add a second resource handler alongside the gallery one
        registry.addResourceHandler("/images/menu-items/**")
                .addResourceLocations("file:" + uploadDir + "/menu-items/");
    }
}