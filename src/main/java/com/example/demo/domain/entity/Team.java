package com.example.demo.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    // Flag de controle de status
    @Column(nullable = false)
    private boolean active = true;
}
