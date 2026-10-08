package com.smartlogix.servicioinventario.repository;

import com.smartlogix.servicioinventario.model.StockBodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockBodegaRepository extends JpaRepository<StockBodega, Long> {
    List<StockBodega> findByProductoId(Long productoId);
    List<StockBodega> findByBodegaId(Long bodegaId);
    Optional<StockBodega> findByProductoIdAndBodegaId(Long productoId, Long bodegaId);
}