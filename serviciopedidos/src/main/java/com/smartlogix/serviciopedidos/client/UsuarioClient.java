package com.smartlogix.serviciopedidos.client;

import com.smartlogix.serviciopedidos.model.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "serviciousuarios")
public interface UsuarioClient {

    @GetMapping("/api/usuarios/{username}")
    Usuario obtenerUsuario(@PathVariable String username);
}