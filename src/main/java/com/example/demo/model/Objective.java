package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "tbl_objetivos") // Nome da tabela no banco
public class Objective {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "obj_id")
    private Long id;

    @Column(name = "titulo", nullable = false) // Não pode ser nulo
    private String title;

    @Column(name = "descricao")
    private String description;

    @ManyToOne
    @JoinColumn(name = "fk_time", nullable = false) // Time deve existir
    private Team team;

    @ManyToMany
    @JoinTable(
            name = "objective_cycles",
            joinColumns = @JoinColumn(name = "objective_id"),
            inverseJoinColumns = @JoinColumn(name = "cycle_id")
    )
    private Set<Cycle> cycles = new HashSet<>(); // Objetivo pode pertencer a vários ciclos
}