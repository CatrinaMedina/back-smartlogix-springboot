package com.smartlogix.servicioinventario.services;

import com.smartlogix.servicioinventario.model.Proveedor;
import java.util.List;
import java.util.Optional;

public interface ProveedorService {
    Proveedor guardar(Proveedor proveedor);
    List<Proveedor> listarTodos();
    Optional<Proveedor> buscarPorId(Long id);
    void eliminar(Long id);
}