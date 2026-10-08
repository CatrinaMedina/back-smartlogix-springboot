package com.smartlogix.serviciopedidos.controller;

import com.smartlogix.serviciopedidos.model.Pedido;
import com.smartlogix.serviciopedidos.services.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Procesamiento de pedidos, validacion de stock y trazabilidad")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Crear pedido", description = "Crea un nuevo pedido, valida usuario, producto, stock y genera trazabilidad")
    public ResponseEntity<?> crear(@RequestBody Pedido pedido,
                                    @RequestHeader(value = "X-Username", required = false) String username,
                                    @RequestHeader(value = "X-Rol", required = false) String rol) {
        try {

            if ("USER".equalsIgnoreCase(rol)) {
                pedido.setUsername(username);
            }

            Pedido creado = pedidoService.crear(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Listar pedidos", description = "Obtiene todos los pedidos. Si el usuario es USER, solo ve los propios")
    public ResponseEntity<?> listar(@RequestHeader(value = "X-Username", required = false) String username,
                                     @RequestHeader(value = "X-Rol", required = false) String rol) {
        if ("USER".equalsIgnoreCase(rol)) {
            List<Pedido> propios = pedidoService.listarPorUsername(username);
            return ResponseEntity.ok(propios);
        }
        return ResponseEntity.ok(pedidoService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID", description = "Busca un pedido especifico mediante su identificador")
    public ResponseEntity<?> obtener(@PathVariable Long id,
                                      @RequestHeader(value = "X-Username", required = false) String username,
                                      @RequestHeader(value = "X-Rol", required = false) String rol) {
        Optional<Pedido> pedidoOpt = pedidoService.buscarPorId(id);

        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Pedido no encontrado"));
        }

        Pedido pedido = pedidoOpt.get();

        if ("USER".equalsIgnoreCase(rol) && !pedido.getUsername().equalsIgnoreCase(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("mensaje", "No tienes permiso para ver este pedido"));
        }

        return ResponseEntity.ok(pedido);
    }
}