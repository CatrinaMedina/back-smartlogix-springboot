package com.smartlogix.serviciopedidos.factory;

import com.smartlogix.serviciopedidos.model.Pedido;
import org.springframework.stereotype.Service;

@Service
public class PedidoExpressCreator implements PedidoCreator {

    @Override
    public Pedido crear() {
        Pedido pedido = new Pedido();
        pedido.setTipo("EXPRESS");
        pedido.setEstado("CREADO");
        return pedido;
    }
}