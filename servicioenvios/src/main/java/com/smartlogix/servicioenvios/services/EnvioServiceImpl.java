package com.smartlogix.servicioenvios.services;

import com.smartlogix.servicioenvios.client.TransportistaClient;
import com.smartlogix.servicioenvios.factory.EnvioFactory;
import com.smartlogix.servicioenvios.model.Envio;
import com.smartlogix.servicioenvios.repository.EnvioRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnvioServiceImpl implements EnvioService {

    private final EnvioRepository envioRepository;
    private final EnvioFactory envioFactory;
    private final TransportistaClient transportistaClient;

    public EnvioServiceImpl(EnvioRepository envioRepository,
                            EnvioFactory envioFactory,
                            TransportistaClient transportistaClient) {
        this.envioRepository = envioRepository;
        this.envioFactory = envioFactory;
        this.transportistaClient = transportistaClient;
    }

    @Override
    @CircuitBreaker(name = "servicioenvios", fallbackMethod = "fallbackCrear")
    public Envio crear(Envio envio) {
        transportistaClient.verificarDisponibilidad();

        Envio nuevoEnvio = envioFactory.getCreator(envio.getTipo()).crear(
                envio.getPedidoId(),
                envio.getDireccion()
        );

        return envioRepository.save(nuevoEnvio);
    }

    public Envio fallbackCrear(Envio envio, Throwable ex) {
        Envio nuevoEnvio = envioFactory.getCreator(envio.getTipo()).crear(
                envio.getPedidoId(),
                envio.getDireccion()
        );

        nuevoEnvio.setEstado("SERVICIO_NO_DISPONIBLE");

        return envioRepository.save(nuevoEnvio);
    }

    @Override
    public Optional<Envio> buscarPorId(Long id) {
        return envioRepository.findById(id);
    }

    @Override
    public Optional<Envio> buscarPorPedidoId(Long pedidoId) {
        return envioRepository.findByPedidoId(pedidoId);
    }

    @Override
    public List<Envio> listar() {
        return envioRepository.findAll();
    }

    @Override
    public Envio actualizarEstado(Long id, String nuevoEstado) {
        List<String> estadosValidos = List.of(
                "PENDIENTE",
                "EN_CAMINO",
                "ENTREGADO",
                "CANCELADO",
                "SERVICIO_NO_DISPONIBLE"
        );

        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new RuntimeException("El estado es obligatorio");
        }

        nuevoEstado = nuevoEstado.trim().toUpperCase();

        if (!estadosValidos.contains(nuevoEstado)) {
            throw new RuntimeException("Estado inválido: " + nuevoEstado);
        }

        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envio no encontrado"));

        envio.setEstado(nuevoEstado);

        return envioRepository.save(envio);
    }
}