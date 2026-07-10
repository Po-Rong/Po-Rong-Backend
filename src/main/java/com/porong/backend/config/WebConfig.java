package com.porong.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(
                    "file:" + System.getProperty("user.dir") + "/uploads/",
                    // upload 사진을 위한 클래스패스 경로
                    "classpath:/static/uploads/"
                );
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://127.0.0.1:5500", 
                    "http://localhost:5500",
                    "https://po-rong-frontend.vercel.app"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .maxAge(3600); 
    }

}
