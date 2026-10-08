package com.smartlogix.serviciopedidos.client;

import com.smartlogix.serviciopedidos.model.Producto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "servicioinventario")
public interface InventarioClient {

    @GetMapping("/api/inventario/{id}")
    Producto obtenerProducto(@PathVariable Long id);

    @PutMapping("/api/inventario/{id}/descontar")
    Producto descontarStock(@PathVariable Long id, @RequestBody Map<String, Integer> body);
}