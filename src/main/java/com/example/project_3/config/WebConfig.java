package com.example.project_3.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map /media/** URL pattern to /static/media/ directory
        registry.addResourceHandler("/media/**")
                .addResourceLocations("classpath:/static/media/");
    }
}
