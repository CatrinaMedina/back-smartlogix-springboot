package com.smartlogix.servicioinventario.services;

import com.smartlogix.servicioinventario.model.Producto;
import com.smartlogix.servicioinventario.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertaStockService {

    private final ProductoRepository productoRepository;

    public AlertaStockService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> verificarStockCritico() {
        return productoRepository.findByCantidadLessThanEqualAndTipoStock(0, "CRITICO");
    }

    public List<Producto> verificarStockMinimo() {
        return productoRepository.findAll().stream()
            .filter(p -> p.getCantidad() <= p.getStockMinimo())
            .toList();
    }
}