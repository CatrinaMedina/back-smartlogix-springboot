package com.smartlogix.servicioenvios.factory;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EnvioFactory {

    private final Map<String, EnvioCreator> creators;

    public EnvioFactory(List<EnvioCreator> creatorList) {
        this.creators = creatorList.stream()
                .collect(Collectors.toMap(
                        c -> c instanceof EnvioExpressCreator ? "EXPRESS" : "NORMAL",
                        c -> c
                ));
    }

    public EnvioCreator getCreator(String tipo) {
        String key = (tipo != null && tipo.equalsIgnoreCase("EXPRESS")) ? "EXPRESS" : "NORMAL";
        return creators.get(key);
    }
}