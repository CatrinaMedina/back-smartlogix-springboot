package com.smartlogix.servicioinventario.services;

import com.smartlogix.servicioinventario.config.RabbitMQConfig;
import com.smartlogix.servicioinventario.model.Producto;
import com.smartlogix.servicioinventario.repository.ProductoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public Producto guardar(Producto producto) {
        producto.setTipoStock(normalizarTipoStock(producto.getTipoStock()));

        Producto guardado = productoRepository.save(producto);

        notificarStockCritico(guardado);

        return guardado;
    }

    @Override
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto actualizar(Long id, Producto producto) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setCantidad(producto.getCantidad());
        existente.setPrecio(producto.getPrecio());
        existente.setTipoStock(normalizarTipoStock(producto.getTipoStock()));
        existente.setStockMinimo(producto.getStockMinimo());
        existente.setBodega(producto.getBodega());
        existente.setProveedor(producto.getProveedor());

        Producto actualizado = productoRepository.save(existente);

        notificarStockCritico(actualizado);

        return actualizado;
    }

    @Override
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return productoRepository.existsByNombre(nombre);
    }

    @Override
    public List<Producto> listarPorTipoStock(String tipoStock) {
        return productoRepository.findByTipoStock(normalizarTipoStock(tipoStock));
    }

    @Override
    public List<Producto> listarStockCriticoAlerta() {
        return productoRepository.findAll().stream()
                .filter(producto -> producto.getCantidad() <= producto.getStockMinimo())
                .toList();
    }

    @Override
    public Producto descontarStock(Long id, Integer cantidad) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (cantidad == null || cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        if (producto.getCantidad() < cantidad) {
            throw new RuntimeException("Cantidad insuficiente en inventario");
        }

        producto.setCantidad(producto.getCantidad() - cantidad);

        Producto actualizado = productoRepository.save(producto);

        notificarStockCritico(actualizado);

        return actualizado;
    }

    private String normalizarTipoStock(String tipoStock) {
        if (tipoStock == null || tipoStock.isBlank()) {
            return "VENTA";
        }

        String tipoNormalizado = tipoStock.trim().toUpperCase();

        if (!tipoNormalizado.equals("VENTA") && !tipoNormalizado.equals("CRITICO")) {
            throw new RuntimeException("Tipo de stock inválido. Use: VENTA o CRITICO");
        }

        return tipoNormalizado;
    }

    private void notificarStockCritico(Producto producto) {
        if (producto.getCantidad() <= producto.getStockMinimo()) {
            Map<String, Object> evento = new HashMap<>();
            evento.put("id", producto.getId());
            evento.put("nombre", producto.getNombre());
            evento.put("cantidad", producto.getCantidad());
            evento.put("stockMinimo", producto.getStockMinimo());

            if (producto.getProveedor() != null) {
                evento.put("proveedorId", producto.getProveedor().getId());
                evento.put("proveedorNombre", producto.getProveedor().getNombre());
            } else {
                evento.put("proveedorId", null);
                evento.put("proveedorNombre", "Sin proveedor asignado");
            }

            try {
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.STOCK_EXCHANGE,
                        RabbitMQConfig.STOCK_ROUTING_KEY,
                        evento
                );
            } catch (Exception e) {
                System.out.println("Error al enviar alerta de stock crítico: " + e.getMessage());
            }
        }
    }
}