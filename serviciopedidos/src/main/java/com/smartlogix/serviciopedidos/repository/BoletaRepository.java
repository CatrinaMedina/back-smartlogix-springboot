package com.smartlogix.serviciopedidos.repository;

import com.smartlogix.serviciopedidos.model.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {
    Optional<Boleta> findByPedidoId(Long pedidoId);
}