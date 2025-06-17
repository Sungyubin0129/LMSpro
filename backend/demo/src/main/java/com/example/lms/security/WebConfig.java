package com.example.lms.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // 또는 /** 로 모두 허용 가능
                        .allowedOrigins("http://localhost:5173") // 프론트엔드 개발 서버 주소
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true); // 필요시
            }
        };
    }
}
