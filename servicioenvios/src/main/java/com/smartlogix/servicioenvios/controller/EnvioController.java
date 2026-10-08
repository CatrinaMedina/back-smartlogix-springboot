package com.smartlogix.servicioenvios.controller;

import com.smartlogix.servicioenvios.model.Envio;
import com.smartlogix.servicioenvios.services.EnvioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/envios")
@Tag(name = "Envios", description = "Coordinacion de envios, estados, trazabilidad y seguimiento por pedido")
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @PostMapping
    @Operation(summary = "Crear envio", description = "Crea un envio asociado a un pedido y registra su direccion de despacho")
    public ResponseEntity<?> crear(@RequestBody Envio envio) {
        if (envio.getPedidoId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "El pedidoId es obligatorio"));
        }
        if (envio.getDireccion() == null || envio.getDireccion().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "La dirección es obligatoria"));
        }
        Envio creado = envioService.crear(envio);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping
    @Operation(summary = "Listar envios", description = "Obtiene todos los envios registrados en el sistema")
    public ResponseEntity<List<Envio>> listar() {
        return ResponseEntity.ok(envioService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener envio por ID", description = "Busca un envio especifico mediante su identificador")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        return envioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pedido/{pedidoId}")
    @Operation(summary = "Obtener envio por pedido", description = "Busca el envio asociado a un pedido especifico")
    public ResponseEntity<?> obtenerPorPedido(@PathVariable Long pedidoId) {
        return envioService.buscarPorPedidoId(pedidoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del envio", description = "Actualiza el estado de un envio, por ejemplo PENDIENTE, EN_CAMINO, ENTREGADO o CANCELADO")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            Envio actualizado = envioService.actualizarEstado(id, body.get("estado"));
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }
}