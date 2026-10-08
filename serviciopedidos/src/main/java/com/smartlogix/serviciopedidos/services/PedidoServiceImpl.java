package com.smartlogix.serviciopedidos.services;

import com.smartlogix.serviciopedidos.client.EnvioClient;
import com.smartlogix.serviciopedidos.client.InventarioClient;
import com.smartlogix.serviciopedidos.client.NotificacionClient;
import com.smartlogix.serviciopedidos.client.UsuarioClient;
import com.smartlogix.serviciopedidos.factory.PedidoFactory;
import com.smartlogix.serviciopedidos.model.Envio;
import com.smartlogix.serviciopedidos.model.Notificacion;
import com.smartlogix.serviciopedidos.model.Pedido;
import com.smartlogix.serviciopedidos.model.Producto;
import com.smartlogix.serviciopedidos.model.Usuario;
import com.smartlogix.serviciopedidos.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private InventarioClient inventarioClient;

    @Autowired
    private EnvioClient envioClient;

    @Autowired
    private NotificacionClient notificacionClient;

    @Autowired
    private PedidoFactory pedidoFactory;

    @Autowired
    private BoletaService boletaService;

    @Override
    public Pedido crear(Pedido pedido) {

        Usuario usuario;
        try {
            usuario = usuarioClient.obtenerUsuario(pedido.getUsername());
        } catch (Exception e) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Producto producto;
        try {
            producto = inventarioClient.obtenerProducto(pedido.getProductoId());
        } catch (Exception e) {
            throw new RuntimeException("Producto no encontrado");
        }

        if (pedido.getCantidad() == null || pedido.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad del pedido debe ser mayor a cero");
        }

        if (pedido.getDireccionEnvio() == null || pedido.getDireccionEnvio().isBlank()) {
            throw new RuntimeException("La dirección de envío es obligatoria");
        }

        if (producto.getCantidad() < pedido.getCantidad()) {
            throw new RuntimeException("Cantidad insuficiente en inventario");
        }

        String tipo = pedido.getTipo() != null ? pedido.getTipo().toUpperCase() : "NORMAL";

        Pedido pedidoNuevo = pedidoFactory.getCreator(tipo).crear();
        pedidoNuevo.setUsername(pedido.getUsername());
        pedidoNuevo.setProductoId(pedido.getProductoId());
        pedidoNuevo.setCantidad(pedido.getCantidad());
        pedidoNuevo.setDireccionEnvio(pedido.getDireccionEnvio());

        Pedido guardado = pedidoRepository.save(pedidoNuevo);

        try {
            inventarioClient.descontarStock(
                    pedido.getProductoId(),
                    Map.of("cantidad", pedido.getCantidad())
            );
        } catch (Exception e) {
            throw new RuntimeException("No se pudo descontar el stock del inventario");
        }

        try {
            double precioNeto = producto.getPrecio() * pedido.getCantidad();
            boletaService.generarBoleta(guardado.getId(), guardado.getUsername(), precioNeto);
        } catch (Exception e) {
            System.out.println("Error al generar boleta: " + e.getMessage());
        }

        try {
            Envio envio = new Envio();
            envio.setPedidoId(guardado.getId());
            envio.setDireccion(guardado.getDireccionEnvio());
            envio.setTipo(guardado.getTipo());
            envioClient.crearEnvio(envio);
        } catch (Exception e) {
            System.out.println("Error al crear envio: " + e.getMessage());
        }

        try {
            Notificacion notificacion = new Notificacion();
            notificacion.setUsuarioId(usuario.getId());
            notificacion.setMensaje("Tu pedido #" + guardado.getId() + " fue creado correctamente. Producto: "
                    + producto.getNombre() + " | Cantidad: " + pedido.getCantidad()
                    + " | Correo:" + usuario.getCorreo());
            notificacion.setTipo("PEDIDO");
            notificacionClient.crearNotificacion(notificacion);
        } catch (Exception e) {
            System.out.println("Error al crear notificacion: " + e.getMessage());
        }

        return guardado;
    }

    @Override
    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    @Override
    public List<Pedido> listarPorUsername(String username) {
        return pedidoRepository.findByUsername(username);
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }
}
