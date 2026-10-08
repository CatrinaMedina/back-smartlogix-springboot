package com.smartlogix.servicioinventario.services;

import com.smartlogix.servicioinventario.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    Producto guardar(Producto producto);
    List<Producto> listarTodos();
    Optional<Producto> buscarPorId(Long id);
    Producto actualizar(Long id, Producto producto);
    void eliminar(Long id);
    boolean existePorNombre(String nombre);
    List<Producto> listarPorTipoStock(String tipoStock);
    List<Producto> listarStockCriticoAlerta();
    Producto descontarStock(Long id, Integer cantidad);
}