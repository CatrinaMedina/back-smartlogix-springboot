package com.smartlogix.servicioinventario.controller;

import com.smartlogix.servicioinventario.model.Proveedor;
import com.smartlogix.servicioinventario.services.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/proveedores")
@Tag(name = "Proveedores", description = "Gestion de proveedores asociados a productos del inventario")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @PostMapping
    @Operation(summary = "Crear proveedor", description = "Registra un nuevo proveedor para los productos del inventario")
    public ResponseEntity<?> crear(@RequestBody Proveedor proveedor) {
        Proveedor guardado = proveedorService.guardar(proveedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @GetMapping
    @Operation(summary = "Listar proveedores", description = "Obtiene todos los proveedores registrados")
    public ResponseEntity<List<Proveedor>> listar() {
        return ResponseEntity.ok(proveedorService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener proveedor por ID", description = "Busca un proveedor especifico mediante su identificador")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        return proveedorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar proveedor", description = "Elimina un proveedor registrado segun su identificador")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (proveedorService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Proveedor no encontrado"));
        }
        proveedorService.eliminar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Proveedor eliminado correctamente"));
    }
}