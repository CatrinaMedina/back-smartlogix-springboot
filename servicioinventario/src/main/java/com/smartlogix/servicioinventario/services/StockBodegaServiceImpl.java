package com.smartlogix.servicioinventario.services;

import com.smartlogix.servicioinventario.model.Bodega;
import com.smartlogix.servicioinventario.model.Producto;
import com.smartlogix.servicioinventario.model.StockBodega;
import com.smartlogix.servicioinventario.repository.BodegaRepository;
import com.smartlogix.servicioinventario.repository.ProductoRepository;
import com.smartlogix.servicioinventario.repository.StockBodegaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockBodegaServiceImpl implements StockBodegaService {

    @Autowired
    private StockBodegaRepository stockBodegaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private BodegaRepository bodegaRepository;

    @Override
    public StockBodega asignarStock(Long productoId, Long bodegaId, int cantidad) {
        if (cantidad < 0) {
            throw new RuntimeException("La cantidad no puede ser negativa");
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Bodega bodega = bodegaRepository.findById(bodegaId)
                .orElseThrow(() -> new RuntimeException("Bodega no encontrada"));

        StockBodega existente = stockBodegaRepository
                .findByProductoIdAndBodegaId(productoId, bodegaId)
                .orElse(null);

        if (existente != null) {
            existente.setCantidad(cantidad);
            return stockBodegaRepository.save(existente);
        }

        StockBodega nuevo = new StockBodega();
        nuevo.setProducto(producto);
        nuevo.setBodega(bodega);
        nuevo.setCantidad(cantidad);

        return stockBodegaRepository.save(nuevo);
    }

    @Override
    public List<StockBodega> listarPorProducto(Long productoId) {
        return stockBodegaRepository.findByProductoId(productoId);
    }

    @Override
    public List<StockBodega> listarPorBodega(Long bodegaId) {
        return stockBodegaRepository.findByBodegaId(bodegaId);
    }

    @Override
    public StockBodega actualizarCantidad(Long productoId, Long bodegaId, int cantidad) {
        if (cantidad < 0) {
            throw new RuntimeException("La cantidad no puede ser negativa");
        }

        StockBodega stock = stockBodegaRepository
                .findByProductoIdAndBodegaId(productoId, bodegaId)
                .orElseThrow(() -> new RuntimeException("Registro de stock no encontrado"));

        stock.setCantidad(cantidad);

        return stockBodegaRepository.save(stock);
    }

    @Override
    public void eliminar(Long id) {
        if (!stockBodegaRepository.existsById(id)) {
            throw new RuntimeException("Registro de stock no encontrado");
        }
        stockBodegaRepository.deleteById(id);
    }
}