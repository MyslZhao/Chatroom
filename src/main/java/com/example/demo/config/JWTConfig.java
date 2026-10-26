package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "jwt")
public class JWTConfig {
    private String secret;
    private Long expiration;
    private Long refreshExpiration;
    private String header;
    private String tokenPrefix;
}