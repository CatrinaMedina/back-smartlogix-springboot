package com.smartlogix.servicioinventario.services;

import com.smartlogix.servicioinventario.model.Bodega;
import java.util.List;
import java.util.Optional;

public interface BodegaService {
    Bodega guardar(Bodega bodega);
    List<Bodega> listarTodas();
    Optional<Bodega> buscarPorId(Long id);
    void eliminar(Long id);
}