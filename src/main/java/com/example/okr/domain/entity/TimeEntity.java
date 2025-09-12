package com.example.okr.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity (name = "team")
@Data
public class TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "name")
    private String Nome;

    // Flag de controle de status
    @Column(name = "is_active", nullable = false)
    private boolean flagActivo = true;
}
