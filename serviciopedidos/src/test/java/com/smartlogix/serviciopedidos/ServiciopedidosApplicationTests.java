package com.smartlogix.serviciopedidos;

import com.smartlogix.serviciopedidos.client.EnvioClient;
import com.smartlogix.serviciopedidos.client.InventarioClient;
import com.smartlogix.serviciopedidos.client.NotificacionClient;
import com.smartlogix.serviciopedidos.client.UsuarioClient;
import com.smartlogix.serviciopedidos.factory.PedidoCreator;
import com.smartlogix.serviciopedidos.factory.PedidoFactory;
import com.smartlogix.serviciopedidos.model.Pedido;
import com.smartlogix.serviciopedidos.model.Producto;
import com.smartlogix.serviciopedidos.model.Usuario;
import com.smartlogix.serviciopedidos.repository.PedidoRepository;
import com.smartlogix.serviciopedidos.services.BoletaService;
import com.smartlogix.serviciopedidos.services.PedidoServiceImpl;
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
class ServiciopedidosApplicationTests {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private InventarioClient inventarioClient;

    @Mock
    private EnvioClient envioClient;

    @Mock
    private NotificacionClient notificacionClient;

    @Mock
    private PedidoFactory pedidoFactory;

    @Mock
    private BoletaService boletaService;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private Pedido pedido;
    private Usuario usuario;
    private Producto producto;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("anais");

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop");
        producto.setCantidad(10);
        producto.setPrecio(999.99);

        pedido = new Pedido();
        pedido.setUsername("anais");
        pedido.setProductoId(1L);
        pedido.setCantidad(2);
        pedido.setDireccionEnvio("Calle 123");
        pedido.setTipo("NORMAL");
        pedido.setEstado("CREADO");
    }

    @Test
    void crear_debeCrearPedidoCorrectamente() {
        Pedido pedidoGuardado = new Pedido();
        pedidoGuardado.setId(1L);
        pedidoGuardado.setUsername("anais");
        pedidoGuardado.setProductoId(1L);
        pedidoGuardado.setCantidad(2);
        pedidoGuardado.setDireccionEnvio("Calle 123");
        pedidoGuardado.setEstado("CREADO");
        pedidoGuardado.setTipo("NORMAL");

        PedidoCreator creatorMock = mock(PedidoCreator.class);
        when(creatorMock.crear()).thenReturn(pedidoGuardado);

        when(usuarioClient.obtenerUsuario("anais")).thenReturn(usuario);
        when(inventarioClient.obtenerProducto(1L)).thenReturn(producto);
        when(pedidoFactory.getCreator("NORMAL")).thenReturn(creatorMock);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoGuardado);

        Pedido resultado = pedidoService.crear(pedido);

        assertNotNull(resultado);
        assertEquals("anais", resultado.getUsername());
        assertEquals("CREADO", resultado.getEstado());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void crear_debeLanzarExcepcionCuandoUsuarioNoExiste() {
        when(usuarioClient.obtenerUsuario("anais")).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> pedidoService.crear(pedido));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void crear_debeLanzarExcepcionCuandoProductoNoExiste() {
        when(usuarioClient.obtenerUsuario("anais")).thenReturn(usuario);
        when(inventarioClient.obtenerProducto(1L)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> pedidoService.crear(pedido));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void crear_debeLanzarExcepcionCuandoStockInsuficiente() {
        producto.setCantidad(1);
        pedido.setCantidad(5);

        when(usuarioClient.obtenerUsuario("anais")).thenReturn(usuario);
        when(inventarioClient.obtenerProducto(1L)).thenReturn(producto);

        assertThrows(RuntimeException.class, () -> pedidoService.crear(pedido));
    }

    @Test
    void crear_debeLanzarExcepcionCuandoCantidadEsCero() {
        pedido.setCantidad(0);

        when(usuarioClient.obtenerUsuario("anais")).thenReturn(usuario);
        when(inventarioClient.obtenerProducto(1L)).thenReturn(producto);

        assertThrows(RuntimeException.class, () -> pedidoService.crear(pedido));
    }

    @Test
    void crear_debeLanzarExcepcionCuandoDireccionEsVacia() {
        pedido.setDireccionEnvio("");

        when(usuarioClient.obtenerUsuario("anais")).thenReturn(usuario);
        when(inventarioClient.obtenerProducto(1L)).thenReturn(producto);

        assertThrows(RuntimeException.class, () -> pedidoService.crear(pedido));
    }

    @Test
    void listar_debeRetornarListaDePedidos() {
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));

        List<Pedido> resultado = pedidoService.listar();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorId_debeRetornarPedidoCuandoExiste() {
        pedido.setId(1L);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Optional<Pedido> resultado = pedidoService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
    }

    @Test
    void buscarPorId_debeRetornarVacioCuandoNoExiste() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Pedido> resultado = pedidoService.buscarPorId(99L);

        assertFalse(resultado.isPresent());
    }
}