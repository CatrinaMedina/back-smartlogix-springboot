package com.smartlogix.servicionotificaciones.services;

import com.smartlogix.servicionotificaciones.model.Notificacion;

import java.util.List;
import java.util.Optional;

public interface NotificacionService {

    Notificacion crear(Notificacion notificacion);

    List<Notificacion> enviarPorRol(
            String rolDestino,
            String asunto,
            String mensaje,
            String remitente,
            String tipo
    );

    Notificacion enviarPorUsuario(
            Long usuarioId,
            String correoDestino,
            String usernameDestino,
            String asunto,
            String mensaje,
            String remitente,
            String tipo
    );

    List<Notificacion> listar();

    List<Notificacion> listarPorUsuario(Long usuarioId);

    Optional<Notificacion> buscarPorId(Long id);

    void eliminar(Long id);
}