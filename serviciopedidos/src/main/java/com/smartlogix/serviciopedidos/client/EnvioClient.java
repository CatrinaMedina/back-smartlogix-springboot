package com.smartlogix.serviciopedidos.client;

import com.smartlogix.serviciopedidos.model.Envio;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "servicioenvios")
public interface EnvioClient {

    @PostMapping("/api/envios")
    Envio crearEnvio(@RequestBody Envio envio);
}