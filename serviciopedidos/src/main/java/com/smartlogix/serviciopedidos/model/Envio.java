package com.smartlogix.serviciopedidos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Envio {
    private Long id;
    private Long pedidoId;
    private String direccion;
    private String estado;
    private String tipo;
}