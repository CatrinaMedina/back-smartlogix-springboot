package com.smartlogix.servicioinventario.controller;

import com.smartlogix.servicioinventario.model.StockBodega;
import com.smartlogix.servicioinventario.services.StockBodegaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventario/stock-bodega")
@Tag(name = "StockBodega", description = "Gestion del stock de un producto distribuido en multiples bodegas o sucursales")
public class StockBodegaController {

    @Autowired
    private StockBodegaService stockBodegaService;

    @PostMapping
    @Operation(summary = "Asignar stock a bodega", description = "Asigna o actualiza la cantidad de un producto en una bodega especifica")
    public ResponseEntity<?> asignar(@RequestBody Map<String, Object> body) {
        try {
            Long productoId = Long.valueOf(body.get("productoId").toString());
            Long bodegaId = Long.valueOf(body.get("bodegaId").toString());
            int cantidad = Integer.parseInt(body.get("cantidad").toString());

            StockBodega resultado = stockBodegaService.asignarStock(productoId, bodegaId, cantidad);
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Listar stock por producto", description = "Obtiene el stock de un producto en todas las bodegas donde esta presente")
    public ResponseEntity<List<StockBodega>> listarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(stockBodegaService.listarPorProducto(productoId));
    }

    @GetMapping("/bodega/{bodegaId}")
    @Operation(summary = "Listar stock por bodega", description = "Obtiene todos los productos y sus cantidades en una bodega especifica")
    public ResponseEntity<List<StockBodega>> listarPorBodega(@PathVariable Long bodegaId) {
        return ResponseEntity.ok(stockBodegaService.listarPorBodega(bodegaId));
    }

    @PutMapping
    @Operation(summary = "Actualizar cantidad de stock", description = "Actualiza la cantidad de un producto en una bodega especifica")
    public ResponseEntity<?> actualizar(@RequestBody Map<String, Object> body) {
        try {
            Long productoId = Long.valueOf(body.get("productoId").toString());
            Long bodegaId = Long.valueOf(body.get("bodegaId").toString());
            int cantidad = Integer.parseInt(body.get("cantidad").toString());

            StockBodega resultado = stockBodegaService.actualizarCantidad(productoId, bodegaId, cantidad);
            return ResponseEntity.ok(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar registro de stock", description = "Elimina la asignacion de stock de un producto en una bodega")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            stockBodegaService.eliminar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Registro eliminado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }
}