package com.smartlogix.servicioinventario.services;

import com.smartlogix.servicioinventario.model.StockBodega;
import java.util.List;

public interface StockBodegaService {
    StockBodega asignarStock(Long productoId, Long bodegaId, int cantidad);
    List<StockBodega> listarPorProducto(Long productoId);
    List<StockBodega> listarPorBodega(Long bodegaId);
    StockBodega actualizarCantidad(Long productoId, Long bodegaId, int cantidad);
    void eliminar(Long id);
}