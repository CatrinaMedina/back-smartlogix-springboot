package com.smartlogix.serviciousuarios.controller;

import com.smartlogix.serviciousuarios.model.Usuario;
import com.smartlogix.serviciousuarios.security.JwtUtils;
import com.smartlogix.serviciousuarios.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios, registro, autenticación y consulta de datos")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/registrar")
    @Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario en el sistema SmartLogix")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {

        if (usuario.getUsername() == null || usuario.getUsername().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "El username es obligatorio"));
        }

        if (usuario.getPassword() == null || usuario.getPassword().length() < 8) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "La contraseña debe tener al menos 8 caracteres"));
        }

        if (usuario.getCorreo() == null || usuario.getCorreo().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "El correo es obligatorio"));
        }

        String correo = usuario.getCorreo().toLowerCase();

        if (!correo.matches("^[\\w.+-]+@(gmail\\.com|duocuc\\.cl|hotmail\\.com)$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "Solo se permiten correos @gmail.com, @duocuc.cl o @hotmail.com"));
        }

        if (usuarioService.existePorUsername(usuario.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("mensaje", "El username ya existe"));
        }

        if (usuarioService.existePorCorreo(correo)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("mensaje", "El correo ya está registrado"));
        }

        if (usuario.getRol() == null || usuario.getRol().isBlank()) {
            usuario.setRol("USER");
        }

        List<String> rolesValidos = List.of("ADMIN", "VENDEDOR", "USER", "PROVEEDOR");

        if (!rolesValidos.contains(usuario.getRol().toUpperCase())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "Rol inválido. Use: ADMIN, VENDEDOR, USER o PROVEEDOR"));
        }

        usuario.setRol(usuario.getRol().toUpperCase());
        usuario.setCorreo(correo);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        Usuario guardado = usuarioService.guardar(usuario);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Usuario registrado correctamente");
        respuesta.put("id", guardado.getId());
        respuesta.put("username", guardado.getUsername());
        respuesta.put("correo", guardado.getCorreo());
        respuesta.put("rol", guardado.getRol());

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Valida las credenciales del usuario y genera un token JWT")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {

        Optional<Usuario> userOpt = usuarioService.buscarPorUsername(usuario.getUsername());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "Credenciales inválidas"));
        }

        Usuario user = userOpt.get();

        if (!passwordEncoder.matches(usuario.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "Credenciales inválidas"));
        }

        String token = jwtUtils.generarToken(user.getId(), user.getUsername(), user.getRol());

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Login correcto");
        respuesta.put("token", token);
        respuesta.put("id", user.getId());
        respuesta.put("username", user.getUsername());
        respuesta.put("correo", user.getCorreo());
        respuesta.put("rol", user.getRol());

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/rol/{rol}")
    @Operation(summary = "Obtener usuarios por rol", description = "Lista los usuarios registrados según su rol")
    public ResponseEntity<?> listarPorRol(@PathVariable String rol) {

        String rolNormalizado = rol.toUpperCase();

        List<String> rolesValidos = List.of("ADMIN", "VENDEDOR", "USER", "PROVEEDOR");

        if (!rolesValidos.contains(rolNormalizado)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "Rol inválido. Use: ADMIN, VENDEDOR, USER o PROVEEDOR"));
        }

        List<Map<String, Object>> usuarios = usuarioService.buscarPorRol(rolNormalizado)
                .stream()
                .map(user -> {
                    Map<String, Object> datos = new HashMap<>();
                    datos.put("id", user.getId());
                    datos.put("username", user.getUsername());
                    datos.put("correo", user.getCorreo());
                    datos.put("rol", user.getRol());
                    return datos;
                })
                .toList();

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{username}")
    @Operation(summary = "Obtener usuario por username", description = "Busca y devuelve los datos de un usuario según su nombre de usuario")
    public ResponseEntity<?> obtener(@PathVariable String username) {

        Optional<Usuario> userOpt = usuarioService.buscarPorUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Usuario no encontrado"));
        }

        Usuario user = userOpt.get();

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "correo", user.getCorreo(),
                "rol", user.getRol()
        ));
    }
}