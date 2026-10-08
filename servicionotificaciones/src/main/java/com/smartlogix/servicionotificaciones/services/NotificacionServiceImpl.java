package com.smartlogix.servicionotificaciones.services;

import com.smartlogix.servicionotificaciones.client.UsuarioClient;
import com.smartlogix.servicionotificaciones.model.Notificacion;
import com.smartlogix.servicionotificaciones.model.Usuario;
import com.smartlogix.servicionotificaciones.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacionServiceImpl implements NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UsuarioClient usuarioClient;

    @Override
    public Notificacion crear(Notificacion notificacion) {
        validarTipo(notificacion.getTipo());

        Notificacion guardada = notificacionRepository.save(notificacion);

        String mensaje = notificacion.getMensaje();

        if (mensaje != null && mensaje.contains("| Correo:")) {
            String[] partes = mensaje.split("\\| Correo:");

            if (partes.length == 2) {
                String correoUsuario = partes[1].trim();
                String tipo = notificacion.getTipo();

                if ("PEDIDO".equals(tipo)) {
                    emailService.enviarCorreo(
                            correoUsuario,
                            "SmartLogix - Pedido Confirmado",
                            "Hola! Tu pedido ha sido creado exitosamente.\n\n"
                                    + partes[0].trim()
                                    + "\n\nGracias por tu compra en SmartLogix."
                    );
                } else if ("ENVIO".equals(tipo)) {
                    emailService.enviarCorreo(
                            correoUsuario,
                            "SmartLogix - Actualizacion de Envio",
                            "Hola! Tu envio ha sido actualizado.\n\n"
                                    + partes[0].trim()
                    );
                }
            }
        }

        return guardada;
    }

    @Override
    public List<Notificacion> enviarPorRol(
            String rolDestino,
            String asunto,
            String mensaje,
            String remitente,
            String tipo
    ) {
        String rol = rolDestino != null ? rolDestino.toUpperCase() : "";
        String tipoFinal = tipo != null && !tipo.isBlank() ? tipo.toUpperCase() : "PEDIDO";

        validarRol(rol);
        validarTipo(tipoFinal);
        validarAsuntoYMensaje(asunto, mensaje);

        List<Usuario> usuariosDestino = usuarioClient.listarPorRol(rol);

        if (usuariosDestino == null || usuariosDestino.isEmpty()) {
            throw new RuntimeException("No existen usuarios registrados con el rol " + rol);
        }

        String remitenteFinal = obtenerRemitenteFinal(remitente);

        String mensajeFinal = "De: " + remitenteFinal
                + " | Para rol: " + rol
                + " | Asunto: " + asunto
                + " | Mensaje: " + mensaje;

        return usuariosDestino.stream().map(usuario -> {
            Notificacion notificacion = new Notificacion();
            notificacion.setUsuarioId(usuario.getId());
            notificacion.setTipo(tipoFinal);
            notificacion.setMensaje(mensajeFinal);

            Notificacion guardada = notificacionRepository.save(notificacion);

            if (usuario.getCorreo() != null && !usuario.getCorreo().isBlank()) {
                emailService.enviarCorreo(
                        usuario.getCorreo(),
                        "SmartLogix - " + asunto,
                        mensajeFinal
                );
            }

            return guardada;
        }).toList();
    }

    @Override
    public Notificacion enviarPorUsuario(
            Long usuarioId,
            String correoDestino,
            String usernameDestino,
            String asunto,
            String mensaje,
            String remitente,
            String tipo
    ) {
        String tipoFinal = tipo != null && !tipo.isBlank() ? tipo.toUpperCase() : "PEDIDO";

        validarTipo(tipoFinal);
        validarAsuntoYMensaje(asunto, mensaje);

        if (usuarioId == null) {
            throw new RuntimeException("El usuarioId destino es obligatorio");
        }

        if (correoDestino == null || correoDestino.isBlank()) {
            throw new RuntimeException("El correoDestino es obligatorio");
        }

        String remitenteFinal = obtenerRemitenteFinal(remitente);

        String destinoFinal = usernameDestino != null && !usernameDestino.isBlank()
                ? usernameDestino + " <" + correoDestino + ">"
                : correoDestino;

        String mensajeFinal = "De: " + remitenteFinal
                + " | Para: " + destinoFinal
                + " | Asunto: " + asunto
                + " | Mensaje: " + mensaje;

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuarioId(usuarioId);
        notificacion.setTipo(tipoFinal);
        notificacion.setMensaje(mensajeFinal);

        Notificacion guardada = notificacionRepository.save(notificacion);

        emailService.enviarCorreo(
                correoDestino,
                "SmartLogix - " + asunto,
                mensajeFinal
        );

        return guardada;
    }

    private void validarTipo(String tipo) {
        List<String> tiposValidos = List.of("PEDIDO", "ENVIO", "STOCK");

        if (tipo == null || !tiposValidos.contains(tipo.toUpperCase())) {
            throw new RuntimeException("Tipo invalido. Use: PEDIDO, ENVIO o STOCK");
        }
    }

    private void validarRol(String rol) {
        List<String> rolesValidos = List.of("ADMIN", "VENDEDOR", "USER", "PROVEEDOR");

        if (rol == null || !rolesValidos.contains(rol.toUpperCase())) {
            throw new RuntimeException("Rol destino invalido. Use: ADMIN, VENDEDOR, USER o PROVEEDOR");
        }
    }

    private void validarAsuntoYMensaje(String asunto, String mensaje) {
        if (asunto == null || asunto.isBlank()) {
            throw new RuntimeException("El asunto es obligatorio");
        }

        if (mensaje == null || mensaje.isBlank()) {
            throw new RuntimeException("El mensaje es obligatorio");
        }
    }

    private String obtenerRemitenteFinal(String remitente) {
        return remitente != null && !remitente.isBlank()
                ? remitente
                : "Sistema";
    }

    @Override
    public List<Notificacion> listar() {
        return notificacionRepository.findAll();
    }

    @Override
    public List<Notificacion> listarPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Optional<Notificacion> buscarPorId(Long id) {
        return notificacionRepository.findById(id);
    }

    @Override
    public void eliminar(Long id) {
        if (!notificacionRepository.existsById(id)) {
            throw new RuntimeException("Notificacion no encontrada");
        }

        notificacionRepository.deleteById(id);
    }
}