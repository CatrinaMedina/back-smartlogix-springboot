package com.smartlogix.serviciopedidos.controller;

import com.smartlogix.serviciopedidos.services.BoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boletas")
@Tag(name = "Boletas", description = "Consulta de boletas generadas a partir de pedidos")
public class BoletaController {

    private final BoletaService boletaService;

    public BoletaController(BoletaService boletaService) {
        this.boletaService = boletaService;
    }

    @GetMapping("/pedido/{pedidoId}")
    @Operation(summary = "Obtener boleta por pedido", description = "Busca la boleta asociada a un pedido especifico")
    public ResponseEntity<?> obtenerPorPedido(@PathVariable Long pedidoId) {
        return boletaService.buscarPorPedidoId(pedidoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}