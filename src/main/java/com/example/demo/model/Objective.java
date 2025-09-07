package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "tbl_objetivos") // Nome da tabela no banco
@SQLDelete(sql = "UPDATE tbl_objectives SET deleted = true WHERE obj_id = ?")
@FilterDef(name = "deletedFilter", defaultCondition = "deleted = false")
@Filters({
        @Filter(name = "deletedFilter")
})
public class Objective {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "obj_id")
    private Long id;

    @Column(name = "titulo", nullable = false) // Não pode ser nulo
    private String title;

    @Column(name = "descricao")
    private String description;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private boolean deleted = false;

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