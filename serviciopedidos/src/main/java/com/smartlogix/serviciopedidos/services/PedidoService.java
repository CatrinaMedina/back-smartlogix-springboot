package com.smartlogix.serviciopedidos.services;

import com.smartlogix.serviciopedidos.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoService {
    Pedido crear(Pedido pedido);
    List<Pedido> listar();
    List<Pedido> listarPorUsername(String username);
    Optional<Pedido> buscarPorId(Long id);
}