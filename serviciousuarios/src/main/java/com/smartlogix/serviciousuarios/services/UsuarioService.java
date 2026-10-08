package com.smartlogix.serviciousuarios.services;

import com.smartlogix.serviciousuarios.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorUsername(String username);

    List<Usuario> buscarPorRol(String rol);

    boolean existePorUsername(String username);

    boolean existePorCorreo(String correo);
}