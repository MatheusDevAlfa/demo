package com.example.okr.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity (name = "cycle")
@Data
public class CicloEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "name",  nullable = false)
    private String Nome; // Ex: Q1 2024

    @Column (name = "start_date")
    private LocalDate inicioData;

    @Column (name = "end_date")
    private LocalDate fimData;

    // Flag de controle de status
    @Column(name = "is_active", nullable = false)
    private boolean flagAtivo = true;
}