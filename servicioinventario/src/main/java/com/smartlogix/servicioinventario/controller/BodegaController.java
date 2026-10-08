package com.smartlogix.servicioinventario.controller;

import com.smartlogix.servicioinventario.model.Bodega;
import com.smartlogix.servicioinventario.services.BodegaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bodegas")
@Tag(name = "Bodegas", description = "Gestion de bodegas, tiendas o sucursales asociadas al inventario")
public class BodegaController {

    @Autowired
    private BodegaService bodegaService;

    @PostMapping
    @Operation(summary = "Crear bodega", description = "Registra una nueva bodega, tienda o sucursal")
    public ResponseEntity<?> crear(@RequestBody Bodega bodega) {
        Bodega guardada = bodegaService.guardar(bodega);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    @GetMapping
    @Operation(summary = "Listar bodegas", description = "Obtiene todas las bodegas, tiendas o sucursales registradas")
    public ResponseEntity<List<Bodega>> listar() {
        return ResponseEntity.ok(bodegaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener bodega por ID", description = "Busca una bodega, tienda o sucursal mediante su identificador")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        return bodegaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar bodega", description = "Elimina una bodega, tienda o sucursal segun su identificador")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (bodegaService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Bodega no encontrada"));
        }
        bodegaService.eliminar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Bodega eliminada correctamente"));
    }
}