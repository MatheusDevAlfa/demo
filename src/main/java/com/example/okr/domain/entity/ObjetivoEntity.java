package com.example.okr.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.*;

import java.util.HashSet;
import java.util.Set;

@Entity (name = "objective")
@Data
@SQLDelete(sql = "UPDATE tbl_objectives SET deleted = true WHERE obj_id = ?")
@FilterDef(name = "deletedFilter", defaultCondition = "deleted = false")
@Filters({
        @Filter(name = "deletedFilter")
})
public class ObjetivoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String titulo;

    @Column(name = "description")
    private String descricao;

    @Column(name = "is_active", nullable = false)
    private boolean flagAtivo = true;


    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private TimeEntity timeEntity;

    @ManyToMany
    @JoinTable(
            name = "objective_cycle",
            joinColumns = @JoinColumn(name = "objective_id"),
            inverseJoinColumns = @JoinColumn(name = "cycle_id")
    )
    private Set<CicloEntity> cicloEntity = new HashSet<>(); // Objetivo pode pertencer a vários ciclos
}