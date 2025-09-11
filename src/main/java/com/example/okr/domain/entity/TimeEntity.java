package com.example.okr.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String Nome;

    // Flag de controle de status
    @Column(nullable = false)
    private boolean active = true;
}
