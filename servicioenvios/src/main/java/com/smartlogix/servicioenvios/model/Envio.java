package com.smartlogix.servicioenvios.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pedidoId;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String estado;

    @Column
    private String transportista;

    @Column
    private String tipoTransportista;

    @Column
    private String codigoSeguimiento;

    @Column
    private Integer diasEstimados;

    @Column
    private LocalDate fechaEstimadaEntrega;

    @Column
    private String tipo;
}