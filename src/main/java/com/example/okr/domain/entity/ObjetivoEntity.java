package com.example.okr.domain.entity;

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
public class ObjetivoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "obj_id")
    private Long id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descricao")
    private String descricao;

    @Column(nullable = false)
    private boolean flagAtivo = true;

//    @Column(nullable = false)
//    private boolean Deleta = false;

    @ManyToOne
    @JoinColumn(name = "fk_time", nullable = false)
    private TimeEntity timeEntity;

    @ManyToMany
    @JoinTable(
            name = "objective_cycles",
            joinColumns = @JoinColumn(name = "objective_id"),
            inverseJoinColumns = @JoinColumn(name = "cycle_id")
    )
    private Set<CicloEtity> cicloEntity = new HashSet<>(); // Objetivo pode pertencer a vários ciclos
}