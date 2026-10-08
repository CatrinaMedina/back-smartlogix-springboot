package com.smartlogix.serviciopedidos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Producto {
    private Long id;
    private String nombre;
    private String descripcion;
    private int cantidad;
    private double precio;
}