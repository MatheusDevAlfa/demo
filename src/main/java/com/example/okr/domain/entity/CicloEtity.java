package com.example.okr.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class CicloEtity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String Nome; // Ex: Q1 2024

    @Column
    private LocalDate inicioData;

    @Column
    private LocalDate fimData;

    // Flag de controle de status
    @Column(nullable = false)
    private boolean active = true;
}