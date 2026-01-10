package com.konopka.ApiGetaway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class CorsGlobalConfiguration implements WebFluxConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry corsRegistry) {
//        corsRegistry.addMapping("/**")
//          .allowedOrigins("http://localhost:5173")
//          .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//          .allowedHeaders("*")
//          .allowCredentials(true)
//          .maxAge(3600);
//    }
}