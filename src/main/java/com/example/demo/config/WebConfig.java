package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor authInterceptor;

    @Value("${spring.file.upload-dir}")
    private String uploadDir;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/file/**", "/uploads/**");

    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ApplicationHome home = new ApplicationHome(WebConfig.class);
        File rootDir = home.getDir();

        File uploadFile = new File(rootDir, uploadDir);

        String location = uploadFile.toURI().toString();
        if (!location.endsWith("/")){
            location += "/";
        }

        registry.addResourceHandler("/uploads/**").addResourceLocations(location);
    }
}