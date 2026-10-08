package com.smartlogix.serviciopedidos.factory;

import com.smartlogix.serviciopedidos.model.Pedido;
import org.springframework.stereotype.Service;

@Service
public class PedidoNormalCreator implements PedidoCreator {

    @Override
    public Pedido crear() {
        Pedido pedido = new Pedido();
        pedido.setTipo("NORMAL");
        pedido.setEstado("CREADO");
        return pedido;
    }
}