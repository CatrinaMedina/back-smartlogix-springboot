package com.smartlogix.servicionotificaciones.messaging;

import com.smartlogix.servicionotificaciones.model.Notificacion;
import com.smartlogix.servicionotificaciones.repository.NotificacionRepository;
import com.smartlogix.servicionotificaciones.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StockEventConsumer {

    private final NotificacionRepository notificacionRepository;
    private final EmailService emailService;

    public StockEventConsumer(NotificacionRepository notificacionRepository, EmailService emailService) {
        this.notificacionRepository = notificacionRepository;
        this.emailService = emailService;
    }

    @RabbitListener(queues = "stock.critico.queue")
    public void recibirAlertaStock(Map<String, Object> productoData) {
        String nombreProducto = String.valueOf(productoData.get("nombre"));
        Object cantidad = productoData.get("cantidad");

        String proveedorNombre = productoData.get("proveedorNombre") != null
                ? String.valueOf(productoData.get("proveedorNombre"))
                : "Sin proveedor asignado";

        Notificacion notifAdmin = new Notificacion();
        notifAdmin.setUsuarioId(1L);
        notifAdmin.setTipo("STOCK");
        notifAdmin.setMensaje("Stock critico: producto '" + nombreProducto + "' tiene cantidad "
                + cantidad + ". Contactar proveedor: " + proveedorNombre);
        notificacionRepository.save(notifAdmin);

        emailService.enviarCorreo(
                "admin@duocuc.cl",
                "SmartLogix - Alerta Stock Critico",
                "El producto '" + nombreProducto + "' ha alcanzado stock critico.\n"
                        + "Cantidad actual: " + cantidad + "\n"
                        + "Proveedor a contactar: " + proveedorNombre
        );

        if (productoData.get("proveedorId") != null) {
            Long proveedorId = Long.valueOf(String.valueOf(productoData.get("proveedorId")));

            Notificacion notifProveedor = new Notificacion();
            notifProveedor.setUsuarioId(proveedorId);
            notifProveedor.setTipo("STOCK");
            notifProveedor.setMensaje("Solicitud de reabastecimiento: el producto '" + nombreProducto
                    + "' ha alcanzado stock critico (cantidad: " + cantidad + "). Por favor reabastecer.");
            notificacionRepository.save(notifProveedor);

            emailService.enviarCorreo(
                    "proveedor1@gmail.com",
                    "SmartLogix - Solicitud de Reabastecimiento",
                    "El producto '" + nombreProducto + "' requiere reabastecimiento.\n"
                            + "Cantidad actual: " + cantidad + "\n"
                            + "Proveedor asignado: " + proveedorNombre
            );
        }
    }
}