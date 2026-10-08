package com.smartlogix.servicioenvios.factory;

import com.smartlogix.servicioenvios.model.Envio;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EnvioNormalCreator implements EnvioCreator {

    @Override
    public Envio crear(Long pedidoId, String direccion) {
        Envio envio = new Envio();

        envio.setPedidoId(pedidoId);
        envio.setDireccion(direccion != null && !direccion.isBlank() ? direccion : "Dirección pendiente");
        envio.setEstado("PENDIENTE");

        envio.setTransportista("Chilexpress");
        envio.setTipoTransportista("EMPRESA_EXTERNA");
        envio.setCodigoSeguimiento("TRK-" + pedidoId + "-SLX");
        envio.setDiasEstimados(3);
        envio.setFechaEstimadaEntrega(LocalDate.now().plusDays(3));
        envio.setTipo("NORMAL");

        return envio;
    }
}