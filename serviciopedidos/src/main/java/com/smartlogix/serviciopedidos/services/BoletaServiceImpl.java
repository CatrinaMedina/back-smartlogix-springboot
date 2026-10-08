package com.smartlogix.serviciopedidos.services;

import com.smartlogix.serviciopedidos.model.Boleta;
import com.smartlogix.serviciopedidos.repository.BoletaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BoletaServiceImpl implements BoletaService {

    private static final double IVA = 0.19;

    private final BoletaRepository boletaRepository;

    public BoletaServiceImpl(BoletaRepository boletaRepository) {
        this.boletaRepository = boletaRepository;
    }

    @Override
    public Boleta generarBoleta(Long pedidoId, String username, double precioNeto) {
        Boleta boleta = new Boleta();
        boleta.setPedidoId(pedidoId);
        boleta.setUsername(username);
        boleta.setPrecioNeto(precioNeto);
        boleta.setIva(precioNeto * IVA);
        boleta.setPrecioTotal(precioNeto + (precioNeto * IVA));
        boleta.setFechaEmision(LocalDate.now());
        return boletaRepository.save(boleta);
    }

    @Override
    public Optional<Boleta> buscarPorPedidoId(Long pedidoId) {
        return boletaRepository.findByPedidoId(pedidoId);
    }
}