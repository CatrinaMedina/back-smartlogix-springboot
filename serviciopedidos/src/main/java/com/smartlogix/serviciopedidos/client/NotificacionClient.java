package com.smartlogix.serviciopedidos.client;

import com.smartlogix.serviciopedidos.model.Notificacion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "servicionotificaciones")
public interface NotificacionClient {

    @PostMapping("/api/notificaciones")
    Notificacion crearNotificacion(@RequestBody Notificacion notificacion);
}