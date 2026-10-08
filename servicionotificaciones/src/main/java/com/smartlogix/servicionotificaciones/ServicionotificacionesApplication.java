package com.smartlogix.servicionotificaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ServicionotificacionesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicionotificacionesApplication.class, args);
    }
}