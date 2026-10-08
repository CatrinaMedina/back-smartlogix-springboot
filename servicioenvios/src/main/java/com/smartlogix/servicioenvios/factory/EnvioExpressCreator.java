package com.smartlogix.servicioenvios.factory;

import com.smartlogix.servicioenvios.model.Envio;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EnvioExpressCreator implements EnvioCreator {

    @Override
    public Envio crear(Long pedidoId, String direccion) {
        Envio envio = new Envio();

        envio.setPedidoId(pedidoId);
        envio.setDireccion(direccion != null && !direccion.isBlank() ? direccion : "Dirección pendiente");
        envio.setEstado("PENDIENTE");

        envio.setTransportista("Repartidor SmartLogix");
        envio.setTipoTransportista("PERSONA_NATURAL");
        envio.setCodigoSeguimiento("EXP-" + pedidoId + "-SLX");
        envio.setDiasEstimados(1);
        envio.setFechaEstimadaEntrega(LocalDate.now().plusDays(1));
        envio.setTipo("EXPRESS");

        return envio;
    }
}