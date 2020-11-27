package com.hancom.hanzari.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
	@Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("X-AUTH-TOKEN","Authorization","Access-Control-Allow-Origin","Access-Control-Allow-Credentials")
                .exposedHeaders("Content-Disposition", "X-AUTH-TOKEN","Authorization","Access-Control-Allow-Origin","Access-Control-Allow-Credentials")
                .allowCredentials(false).maxAge(3600);

    }
}