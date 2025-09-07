package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Cycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name; // Ex: Q1 2024

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    // Flag de controle de status
    @Column(nullable = false)
    private boolean active = true;
}