package com.smartlogix.serviciopedidos.services;

import com.smartlogix.serviciopedidos.model.Boleta;
import java.util.Optional;

public interface BoletaService {
    Boleta generarBoleta(Long pedidoId, String username, double precioNeto);
    Optional<Boleta> buscarPorPedidoId(Long pedidoId);
}