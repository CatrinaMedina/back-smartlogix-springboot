package com.smartlogix.servicioinventario.controller;

import com.smartlogix.servicioinventario.model.Producto;
import com.smartlogix.servicioinventario.services.ProductoService;
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
@RequestMapping("/api/inventario")
@Tag(name = "Inventario", description = "Gestion de productos, stock de venta, stock critico, bodegas y proveedores")
public class InventarioController {

    @Autowired
    private ProductoService productoService;

    @PostMapping
    @Operation(summary = "Crear producto", description = "Registra un nuevo producto dentro del inventario")
    public ResponseEntity<?> crear(@RequestBody Producto producto) {
        if (productoService.existePorNombre(producto.getNombre())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("mensaje", "El producto ya existe"));
        }
        Producto guardado = productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @GetMapping
    @Operation(summary = "Listar productos", description = "Obtiene todos los productos registrados en inventario")
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Busca un producto especifico mediante su identificador")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        Optional<Producto> producto = productoService.buscarPorId(id);
        if (producto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Producto no encontrado"));
        }
        return ResponseEntity.ok(producto.get());
    }

    @GetMapping("/stock/{tipoStock}")
    @Operation(summary = "Listar productos por tipo de stock", description = "Lista productos segun tipo de stock, por ejemplo VENTA o CRITICO")
    public ResponseEntity<List<Producto>> listarPorTipo(@PathVariable String tipoStock) {
        return ResponseEntity.ok(productoService.listarPorTipoStock(tipoStock.toUpperCase()));
    }

    @GetMapping("/stock/critico/alertas")
    @Operation(summary = "Listar alertas de stock critico", description = "Obtiene productos cuya cantidad actual se encuentra bajo o igual al stock minimo")
    public ResponseEntity<List<Producto>> alertasStockCritico() {
        return ResponseEntity.ok(productoService.listarStockCriticoAlerta());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza los datos de un producto existente")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            Producto actualizado = productoService.actualizar(id, producto);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Producto no encontrado"));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del inventario segun su identificador")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (productoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Producto no encontrado"));
        }
        productoService.eliminar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Producto eliminado correctamente"));
    }

    @PutMapping("/{id}/descontar")
    @Operation(summary = "Descontar stock", description = "Descuenta una cantidad del stock de un producto cuando se genera un pedido")
    public ResponseEntity<?> descontarStock(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        try {
            Integer cantidad = body.get("cantidad");
            Producto actualizado = productoService.descontarStock(id, cantidad);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }
}