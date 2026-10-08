package com.smartlogix.serviciopedidos.factory;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class PedidoFactory {

    private final Map<String, PedidoCreator> creators;

    public PedidoFactory(List<PedidoCreator> creatorList) {
        this.creators = Map.of(
            "NORMAL", creatorList.stream()
                .filter(c -> c instanceof PedidoNormalCreator)
                .findFirst()
                .orElseThrow(),
            "EXPRESS", creatorList.stream()
                .filter(c -> c instanceof PedidoExpressCreator)
                .findFirst()
                .orElseThrow()
        );
    }

    public PedidoCreator getCreator(String tipo) {
        PedidoCreator creator = creators.get(tipo != null ? tipo.toUpperCase() : "NORMAL");
        if (creator == null) {
            throw new IllegalArgumentException(
                "Tipo de pedido no soportado: " + tipo
            );
        }
        return creator;
    }
}