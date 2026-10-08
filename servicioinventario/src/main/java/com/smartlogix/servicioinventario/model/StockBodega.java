package com.smartlogix.servicioinventario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stock_bodega", uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "bodega_id"}))
public class StockBodega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "bodega_id", nullable = false)
    private Bodega bodega;

    @Column(nullable = false)
    private int cantidad;
}