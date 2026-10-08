package com.smartlogix.servicioinventario.services;

import com.smartlogix.servicioinventario.model.Bodega;
import com.smartlogix.servicioinventario.repository.BodegaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BodegaServiceImpl implements BodegaService {

    @Autowired
    private BodegaRepository bodegaRepository;

    @Override
    public Bodega guardar(Bodega bodega) {
        return bodegaRepository.save(bodega);
    }

    @Override
    public List<Bodega> listarTodas() {
        return bodegaRepository.findAll();
    }

    @Override
    public Optional<Bodega> buscarPorId(Long id) {
        return bodegaRepository.findById(id);
    }

    @Override
    public void eliminar(Long id) {
        bodegaRepository.deleteById(id);
    }
}