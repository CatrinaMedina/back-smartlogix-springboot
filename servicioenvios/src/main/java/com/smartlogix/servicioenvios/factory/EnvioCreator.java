package com.smartlogix.servicioenvios.factory;

import com.smartlogix.servicioenvios.model.Envio;

public interface EnvioCreator {
    Envio crear(Long pedidoId, String direccion);
}