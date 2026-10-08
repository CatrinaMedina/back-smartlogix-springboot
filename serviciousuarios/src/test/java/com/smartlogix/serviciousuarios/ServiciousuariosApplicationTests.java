package com.smartlogix.serviciousuarios;

import com.smartlogix.serviciousuarios.model.Usuario;
import com.smartlogix.serviciousuarios.repository.UsuarioRepository;
import com.smartlogix.serviciousuarios.services.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiciousuariosApplicationTests {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("anais");
        usuario.setPassword("password123");
        usuario.setRol("USER");
        usuario.setCorreo("anais@gmail.com");
    }

    @Test
    void guardar_debeRetornarUsuarioGuardado() {
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = usuarioService.guardar(usuario);

        assertNotNull(resultado);
        assertEquals("anais", resultado.getUsername());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void buscarPorUsername_debeRetornarUsuarioCuandoExiste() {
        when(usuarioRepository.findByUsername("anais")).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.buscarPorUsername("anais");

        assertTrue(resultado.isPresent());
        assertEquals("anais", resultado.get().getUsername());
    }

    @Test
    void buscarPorUsername_debeRetornarVacioCuandoNoExiste() {
        when(usuarioRepository.findByUsername("noexiste")).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.buscarPorUsername("noexiste");

        assertFalse(resultado.isPresent());
    }

    @Test
    void existePorUsername_debeRetornarTrueCuandoExiste() {
        when(usuarioRepository.existsByUsername("anais")).thenReturn(true);

        boolean resultado = usuarioService.existePorUsername("anais");

        assertTrue(resultado);
    }

    @Test
    void existePorUsername_debeRetornarFalseCuandoNoExiste() {
        when(usuarioRepository.existsByUsername("noexiste")).thenReturn(false);

        boolean resultado = usuarioService.existePorUsername("noexiste");

        assertFalse(resultado);
    }
}