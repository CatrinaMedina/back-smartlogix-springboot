package com.smartlogix.servicionotificaciones.client;

import com.smartlogix.servicionotificaciones.model.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "serviciousuarios")
public interface UsuarioClient {

    @GetMapping("/api/usuarios/rol/{rol}")
    List<Usuario> listarPorRol(@PathVariable String rol);
}