package com.smartlogix.servicioenvios.services;

import com.smartlogix.servicioenvios.model.Envio;
import java.util.List;
import java.util.Optional;

public interface EnvioService {
    Envio crear(Envio envio);
    Optional<Envio> buscarPorId(Long id);
    Optional<Envio> buscarPorPedidoId(Long pedidoId);
    List<Envio> listar();
    Envio actualizarEstado(Long id, String nuevoEstado);
}