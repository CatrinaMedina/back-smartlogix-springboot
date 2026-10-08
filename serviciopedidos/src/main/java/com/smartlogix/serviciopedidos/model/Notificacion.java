package com.smartlogix.serviciopedidos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notificacion {
    private Long id;
    private Long usuarioId;
    private String mensaje;
    private String tipo;
}