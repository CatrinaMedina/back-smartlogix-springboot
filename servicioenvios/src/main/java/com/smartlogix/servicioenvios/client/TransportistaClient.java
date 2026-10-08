package com.smartlogix.servicioenvios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "transportista-externo", url = "${transportista.service.url:https://httpbin.org}")
public interface TransportistaClient {

    @GetMapping("/get")
    String verificarDisponibilidad();
}