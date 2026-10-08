package com.smartlogix.servicioinventario;

import com.smartlogix.servicioinventario.model.Producto;
import com.smartlogix.servicioinventario.repository.ProductoRepository;
import com.smartlogix.servicioinventario.services.ProductoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioinventarioApplicationTests {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();

        producto.setId(1L);
        producto.setNombre("Laptop");
        producto.setDescripcion("Laptop gamer");
        producto.setCantidad(10);
        producto.setPrecio(999.99);
        producto.setTipoStock("VENTA");
        producto.setStockMinimo(2);
    }

    @Test
    void guardar_debeRetornarProductoGuardado() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto resultado = productoService.guardar(producto);

        assertNotNull(resultado);
        assertEquals("Laptop", resultado.getNombre());
        assertEquals("VENTA", resultado.getTipoStock());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void guardar_debeNormalizarTipoStockAMayusculas() {
        producto.setTipoStock("venta");
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto resultado = productoService.guardar(producto);

        assertEquals("VENTA", resultado.getTipoStock());
    }

    @Test
    void guardar_debeLanzarExcepcionCuandoTipoStockEsInvalido() {
        producto.setTipoStock("INVALIDO");

        assertThrows(RuntimeException.class, () -> productoService.guardar(producto));
        verify(productoRepository, never()).save(any());
    }

    @Test
    void listarTodos_debeRetornarListaDeProductos() {
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<Producto> resultado = productoService.listarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorId_debeRetornarProductoCuandoExiste() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> resultado = productoService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Laptop", resultado.get().getNombre());
    }

    @Test
    void buscarPorId_debeRetornarVacioCuandoNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Producto> resultado = productoService.buscarPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void eliminar_debeEliminarProducto() {
        doNothing().when(productoRepository).deleteById(1L);

        productoService.eliminar(1L);

        verify(productoRepository, times(1)).deleteById(1L);
    }

    @Test
    void existePorNombre_debeRetornarTrueCuandoExiste() {
        when(productoRepository.existsByNombre("Laptop")).thenReturn(true);

        boolean resultado = productoService.existePorNombre("Laptop");

        assertTrue(resultado);
    }

    @Test
    void descontarStock_debeDescontarCantidadCorrectamente() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto resultado = productoService.descontarStock(1L, 3);

        assertEquals(7, resultado.getCantidad());
    }

    @Test
    void descontarStock_debeLanzarExcepcionCuandoStockInsuficiente() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        assertThrows(RuntimeException.class, () -> productoService.descontarStock(1L, 20));
    }

    @Test
    void descontarStock_debeLanzarExcepcionCuandoCantidadEsCero() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        assertThrows(RuntimeException.class, () -> productoService.descontarStock(1L, 0));
    }

    @Test
    void listarStockCriticoAlerta_debeRetornarProductosConStockBajo() {
        producto.setCantidad(1);
        producto.setStockMinimo(2);
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<Producto> resultado = productoService.listarStockCriticoAlerta();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }
}