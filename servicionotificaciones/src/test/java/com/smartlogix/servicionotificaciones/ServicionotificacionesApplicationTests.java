package com.smartlogix.servicionotificaciones;

import com.smartlogix.servicionotificaciones.client.UsuarioClient;
import com.smartlogix.servicionotificaciones.model.Notificacion;
import com.smartlogix.servicionotificaciones.repository.NotificacionRepository;
import com.smartlogix.servicionotificaciones.services.EmailService;
import com.smartlogix.servicionotificaciones.services.NotificacionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceImplTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private NotificacionServiceImpl notificacionService;

    private Notificacion notificacion;

    @BeforeEach
    void setUp() {
        notificacion = new Notificacion();
        notificacion.setId(1L);
        notificacion.setUsuarioId(1L);
        notificacion.setMensaje("Tu pedido fue creado correctamente.");
        notificacion.setTipo("PEDIDO");
    }

    @Test
    void crear_debeRetornarNotificacionCreada() {
        when(notificacionRepository.save(notificacion)).thenReturn(notificacion);

        Notificacion resultado = notificacionService.crear(notificacion);

        assertNotNull(resultado);
        assertEquals("PEDIDO", resultado.getTipo());
        verify(notificacionRepository, times(1)).save(notificacion);
    }

    @Test
    void crear_debeLanzarExcepcionCuandoTipoEsInvalido() {
        notificacion.setTipo("INVALIDO");

        assertThrows(RuntimeException.class, () -> notificacionService.crear(notificacion));
        verify(notificacionRepository, never()).save(any());
    }

    @Test
    void crear_debeAceptarTipoENVIO() {
        notificacion.setTipo("ENVIO");
        when(notificacionRepository.save(notificacion)).thenReturn(notificacion);

        Notificacion resultado = notificacionService.crear(notificacion);

        assertEquals("ENVIO", resultado.getTipo());
    }

    @Test
    void crear_debeAceptarTipoSTOCK() {
        notificacion.setTipo("STOCK");
        when(notificacionRepository.save(notificacion)).thenReturn(notificacion);

        Notificacion resultado = notificacionService.crear(notificacion);

        assertEquals("STOCK", resultado.getTipo());
    }

    @Test
    void listar_debeRetornarListaDeNotificaciones() {
        when(notificacionRepository.findAll()).thenReturn(List.of(notificacion));

        List<Notificacion> resultado = notificacionService.listar();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void listarPorUsuario_debeRetornarNotificacionesDelUsuario() {
        when(notificacionRepository.findByUsuarioId(1L)).thenReturn(List.of(notificacion));

        List<Notificacion> resultado = notificacionService.listarPorUsuario(1L);

        assertFalse(resultado.isEmpty());
        assertEquals(1L, resultado.get(0).getUsuarioId());
    }

    @Test
    void buscarPorId_debeRetornarNotificacionCuandoExiste() {
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));

        Optional<Notificacion> resultado = notificacionService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Tu pedido fue creado correctamente.", resultado.get().getMensaje());
    }

    @Test
    void buscarPorId_debeRetornarVacioCuandoNoExiste() {
        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Notificacion> resultado = notificacionService.buscarPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void eliminar_debeEliminarNotificacionCuandoExiste() {
        when(notificacionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(notificacionRepository).deleteById(1L);

        assertDoesNotThrow(() -> notificacionService.eliminar(1L));
        verify(notificacionRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminar_debeLanzarExcepcionCuandoNoExiste() {
        when(notificacionRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> notificacionService.eliminar(99L));
        verify(notificacionRepository, never()).deleteById(any());
    }
}