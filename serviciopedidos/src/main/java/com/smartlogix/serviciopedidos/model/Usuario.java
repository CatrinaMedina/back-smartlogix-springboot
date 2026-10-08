package com.smartlogix.serviciopedidos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usuario {
    private Long id;
    private String username;
    private String correo;
    private String rol;
}