package com.smartlogix.servicionotificaciones.controller;

import com.smartlogix.servicionotificaciones.model.Notificacion;
import com.smartlogix.servicionotificaciones.services.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
@Tag(name = "Notificaciones", description = "Gestion de notificaciones para pedidos, envios y alertas de stock critico")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @PostMapping
    @Operation(summary = "Crear notificacion", description = "Crea una notificacion asociada a un usuario")
    public ResponseEntity<?> crear(@RequestBody Notificacion notificacion) {
        if (notificacion.getUsuarioId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "El usuarioId es obligatorio"));
        }

        if (notificacion.getMensaje() == null || notificacion.getMensaje().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "El mensaje es obligatorio"));
        }

        try {
            Notificacion creada = notificacionService.crear(notificacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }

    @PostMapping("/enviar-rol")
    @Operation(summary = "Enviar notificacion por rol", description = "Crea una notificacion interna para todos los usuarios asociados a un rol")
    public ResponseEntity<?> enviarPorRol(@RequestBody Map<String, String> body) {
        String rolDestino = body.get("rolDestino");
        String asunto = body.get("asunto");
        String mensaje = body.get("mensaje");
        String remitente = body.get("remitente");
        String tipo = body.getOrDefault("tipo", "PEDIDO");

        if (rolDestino == null || rolDestino.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "El rolDestino es obligatorio"));
        }

        if (asunto == null || asunto.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "El asunto es obligatorio"));
        }

        if (mensaje == null || mensaje.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "El mensaje es obligatorio"));
        }

        try {
            List<Notificacion> creadas = notificacionService.enviarPorRol(
                    rolDestino,
                    asunto,
                    mensaje,
                    remitente,
                    tipo
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(creadas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }

    @PostMapping("/enviar-usuario")
    @Operation(summary = "Enviar notificacion a usuario", description = "Crea una notificacion interna para un usuario especifico usando su correo registrado")
    public ResponseEntity<?> enviarPorUsuario(@RequestBody Map<String, Object> body) {
        Long usuarioId;

        try {
            Object usuarioIdObj = body.get("usuarioId");

            if (usuarioIdObj == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("mensaje", "El usuarioId es obligatorio"));
            }

            usuarioId = Long.valueOf(String.valueOf(usuarioIdObj));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "El usuarioId debe ser numerico"));
        }

        String correoDestino = obtenerTexto(body, "correoDestino");
        String usernameDestino = obtenerTexto(body, "usernameDestino");
        String asunto = obtenerTexto(body, "asunto");
        String mensaje = obtenerTexto(body, "mensaje");
        String remitente = obtenerTexto(body, "remitente");
        String tipo = obtenerTexto(body, "tipo");

        if (tipo == null || tipo.isBlank()) {
            tipo = "PEDIDO";
        }

        if (correoDestino == null || correoDestino.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "El correoDestino es obligatorio"));
        }

        if (asunto == null || asunto.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "El asunto es obligatorio"));
        }

        if (mensaje == null || mensaje.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "El mensaje es obligatorio"));
        }

        try {
            Notificacion creada = notificacionService.enviarPorUsuario(
                    usuarioId,
                    correoDestino,
                    usernameDestino,
                    asunto,
                    mensaje,
                    remitente,
                    tipo
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Listar notificaciones", description = "Obtiene todas las notificaciones registradas")
    public ResponseEntity<List<Notificacion>> listar() {
        return ResponseEntity.ok(notificacionService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener notificacion por ID", description = "Busca una notificacion especifica mediante su identificador")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        return notificacionService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar notificaciones por usuario", description = "Obtiene todas las notificaciones asociadas a un usuario especifico")
    public ResponseEntity<List<Notificacion>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionService.listarPorUsuario(usuarioId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar notificacion", description = "Elimina una notificacion especifica segun su identificador")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            notificacionService.eliminar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Notificación eliminada"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }

    private String obtenerTexto(Map<String, Object> body, String clave) {
        Object valor = body.get(clave);

        if (valor == null) {
            return null;
        }

        return String.valueOf(valor);
    }
}