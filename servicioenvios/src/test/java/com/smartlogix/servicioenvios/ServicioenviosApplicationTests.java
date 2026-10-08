package com.smartlogix.servicioenvios;

import com.smartlogix.servicioenvios.client.TransportistaClient;
import com.smartlogix.servicioenvios.factory.EnvioCreator;
import com.smartlogix.servicioenvios.factory.EnvioFactory;
import com.smartlogix.servicioenvios.model.Envio;
import com.smartlogix.servicioenvios.repository.EnvioRepository;
import com.smartlogix.servicioenvios.services.EnvioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioenviosApplicationTests {

    @Mock
    private EnvioRepository envioRepository;

    @Mock
    private EnvioFactory envioFactory;

    @Mock
    private TransportistaClient transportistaClient;

    @InjectMocks
    private EnvioServiceImpl envioService;

    private Envio envio;

    @BeforeEach
    void setUp() {
        envio = new Envio();
        envio.setId(1L);
        envio.setPedidoId(1L);
        envio.setDireccion("Calle 123");
        envio.setEstado("PENDIENTE");
    }

    @Test
    void buscarPorId_debeRetornarEnvioCuandoExiste() {
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));

        Optional<Envio> resultado = envioService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Calle 123", resultado.get().getDireccion());
    }

    @Test
    void buscarPorId_debeRetornarVacioCuandoNoExiste() {
        when(envioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Envio> resultado = envioService.buscarPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void buscarPorPedidoId_debeRetornarEnvioCuandoExiste() {
        when(envioRepository.findByPedidoId(1L)).thenReturn(Optional.of(envio));

        Optional<Envio> resultado = envioService.buscarPorPedidoId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getPedidoId());
    }

    @Test
    void listar_debeRetornarListaDeEnvios() {
        when(envioRepository.findAll()).thenReturn(List.of(envio));

        List<Envio> resultado = envioService.listar();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void actualizarEstado_debeActualizarEstadoCorrectamente() {
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);

        Envio resultado = envioService.actualizarEstado(1L, "EN_CAMINO");

        assertEquals("EN_CAMINO", resultado.getEstado());
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    @Test
        void actualizarEstado_debeLanzarExcepcionCuandoEstadoEsInvalido() {
        assertThrows(RuntimeException.class,
            () -> envioService.actualizarEstado(1L, "INVALIDO"));
    }

    @Test
    void actualizarEstado_debeLanzarExcepcionCuandoEstadoEsNulo() {
        assertThrows(RuntimeException.class, () -> envioService.actualizarEstado(1L, null));
    }

    @Test
    void actualizarEstado_debeLanzarExcepcionCuandoEnvioNoExiste() {
        when(envioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> envioService.actualizarEstado(99L, "EN_CAMINO"));
    }

    @Test
    void fallbackCrear_debeCrearEnvioConEstadoNoDisponible() {
        EnvioCreator creatorMock = mock(EnvioCreator.class);
        when(envioFactory.getCreator(any())).thenReturn(creatorMock);
        when(creatorMock.crear(1L, "Calle 123")).thenReturn(envio);
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);

        Envio resultado = envioService.fallbackCrear(envio, new RuntimeException("timeout"));

        assertEquals("SERVICIO_NO_DISPONIBLE", resultado.getEstado());
        verify(envioRepository, times(1)).save(any(Envio.class));
    }
}