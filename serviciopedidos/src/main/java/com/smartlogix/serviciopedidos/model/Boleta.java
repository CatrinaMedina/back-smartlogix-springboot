package com.smartlogix.serviciopedidos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "boletas")
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pedidoId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private double precioNeto;

    @Column(nullable = false)
    private double iva;

    @Column(nullable = false)
    private double precioTotal;

    @Column(nullable = false)
    private LocalDate fechaEmision;
}